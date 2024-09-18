package com.damzxyno.rasdspringapi.core.services;

import com.damzxyno.rasdspringapi.models.AuthorisationMod;
import com.damzxyno.rasdspringapi.models.SecureModel;
import org.springframework.expression.spel.SpelNode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.ast.MethodReference;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.expression.spel.ast.StringLiteral;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.*;

public class SecuritySpELHandler {
    private final SpelParserConfiguration config;
    private final SpelExpressionParser parser;
    private static SecuritySpELHandler INSTANCE;

    private SecuritySpELHandler(){
        config =  new SpelParserConfiguration(true, true);
        parser =  new SpelExpressionParser(config);

    }

    public static synchronized SecuritySpELHandler getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SecuritySpELHandler();
        }
        return INSTANCE;
    }
    public AuthorisationMod parseExpressionAndBuildAuthorisationMod (String expressionString){
        SpelExpression expression = (SpelExpression) parser.parseExpression(expressionString);
        SpelNode ast = expression.getAST();
        Stack<SpelNode> stack = new Stack<>();
        buildNodeStack(stack, ast);
        AuthorisationMod mod =  buildAuthorisationMod(stack);
        return mod;

    }

    private void buildNodeStack (Stack<SpelNode> stack, SpelNode spelNode){
        if (spelNode instanceof MethodReference) {
            stack.add(spelNode);
            return;
        }
        for (int i = 0; i < spelNode.getChildCount(); i++) {
            buildNodeStack(stack, spelNode.getChild(i));
            if (i < spelNode.getChildCount() - 1){
                stack.add(spelNode);
            }
        }
    }

    private AuthorisationMod buildAuthorisationMod(Stack<SpelNode> stack){
        AuthorisationMod mod = new AuthorisationMod();
        SecureModel currentModel = new SecureModel();
        while (!stack.empty()) {
            SpelNode node = stack.pop();
            if (node instanceof OpOr) {
                mod.getRelativeMod().add(currentModel);
                currentModel = new SecureModel();
            } else if (node instanceof MethodReference) {
                if (((MethodReference) node).getName().equals("hasRole")) {
                    addNodeStringChildrenToSet(node, currentModel.getRoles());
                } else if ((((MethodReference) node).getName().equals("hasAnyRole"))) {
                    List<SecureModel> models = addNodeStringChildrenToSet(node, "Roles");
                    mod.getRelativeMod().addAll(models);
                } else if ((((MethodReference) node).getName().equals("hasAuthority"))) {
                    addNodeStringChildrenToSet(node, currentModel.getPermissions());
                } else if ((((MethodReference) node).getName().equals("hasAnyAuthority"))) {
                    List<SecureModel> models = addNodeStringChildrenToSet(node, "Permissions");
                    mod.getRelativeMod().addAll(models);
                }
            }
        }
        if (!currentModel.isEmptyStringAndAuthorisation()){
            if (mod.getRelativeMod().isEmpty() &&
                    mod.getStaticMod().getRoles().isEmpty() &&
                    mod.getStaticMod().getPermissions().isEmpty()){
                mod.setStaticMod(currentModel);
            } else {
                mod.getRelativeMod().add(currentModel);
            }
        }


        return mod;

    }


    private void addNodeStringChildrenToSet(SpelNode node, Set<String> set){
        for (int i = 0; i < node.getChildCount(); i++){
            set.add(((StringLiteral)node.getChild(i)).getOriginalValue());
        }
    }

    private List<SecureModel> addNodeStringChildrenToSet(SpelNode node, String location){
        List<SecureModel> secureModels = new ArrayList<>();
        for (int i = 0; i < node.getChildCount(); i++){
            SecureModel model = new SecureModel();
            if (location.equals("Roles")){
                model.getRoles().add(((StringLiteral)node.getChild(i)).getOriginalValue());
            } else {
                model.getPermissions().add(((StringLiteral)node.getChild(i)).getOriginalValue());
            }
            secureModels.add(model);
        }
        return secureModels;
    }
}
