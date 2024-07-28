package ExtrasForTestPattern.RuleMining;

import com.intellij.psi.*;

import java.util.ArrayList;
import java.util.List;

import static org.paukov.combinatorics.CombinatoricsFactory.createSubSetGenerator;
import static org.paukov.combinatorics.CombinatoricsFactory.createVector;

@SuppressWarnings("WeakerAccess")

public class RuleMiningVisitor_v2 extends JavaRecursiveElementVisitor {
    //v1 -> init
    //v2 -> add all kinds of control-flow-based statements, different Junit statements and other statements here
    //v3 -> fix "-1" error
    //v4 -> new visitMethodCallExpression

    final List<String> tokens;

    public RuleMiningVisitor_v2() {
        this.tokens = new ArrayList<>();
    }

    public List<String> getTokens() {
        return tokens;
    }

    public static List<String> tokensFor(PsiMethod element) {
        RuleMiningVisitor_v2 visitor = new RuleMiningVisitor_v2();
        visitor.tokens.add("start{");
        element.accept(visitor);
        visitor.tokens.add("}end");
        return visitor.getTokens();
    }

    @Override
    public void visitMethodCallExpression(PsiMethodCallExpression expression) {

        if (expression.getText().contains("assert") || expression.getText().contains("Assert")){

            if (expression.getMethodExpression().getReference() !=  null) {

                String name = expression.getMethodExpression().getReferenceName();

                if (name != null) {
                    if (!name.contains("assert") && !name.contains("Assert")) {
                        System.out.println("method: " + expression.getText());
                        super.visitMethodCallExpression(expression);
                    }
                    else{
                        String s = expression.getMethodExpression().getReferenceName();
                        System.out.println(s);
                        if (s != null) {
                            if ("assertEquals".equals(s)) {
                                tokens.add("assertEquals");

                            } else if ("assertNotEquals".equals(s)) {
                                tokens.add("assertNotEquals");

                            } else if ("assertSame".equals(s)) {
                                tokens.add("assertSame");

                            } else if ("assertNotSame".equals(s)) {
                                tokens.add("assertNotSame");

                            } else if ("assertThat".equals(s)) {
                                tokens.add("assertThat");

                            } else if ("assertTrue".equals(s)) {
                                tokens.add("assertTrue");

                            } else if ("assertFalse".equals(s)) {
                                tokens.add("assertFalse");

                            } else if ("assertNotNull".equals(s)) {
                                tokens.add("assertNotNull");

                            } else if ("assertNull".equals(s)) {
                                tokens.add("assertNull");

                            } else if (s.contains("assert") || s.contains("Assert")) {
                                tokens.add("assertionNotJUnit");
                            }
                        }
                    }
                }
            }
        }
        //Get the deepest method call only
        else {
            if (expression.getMethodExpression().getReferenceName() != null) {
                String s = expression.getMethodExpression().getReferenceName();
                System.out.println(s);

                if ("fail".equals(s)) {
                    tokens.add("fail");

                }else {
                    tokens.add("methodCall");
                }
            }
        }
    }

    @Override
    public void visitDeclarationStatement(PsiDeclarationStatement statement) {
        tokens.add("declaration");
    }

    @Override
    public void visitAssertStatement(PsiAssertStatement statement) {
        tokens.add("assert");
    }

    @Override
    public void visitTryStatement(PsiTryStatement statement) {
        tokens.add("try{");
        super.visitStatement(statement);
        tokens.add("}try");
    }

    @Override
    public void visitCatchSection(PsiCatchSection section) {
        tokens.add("catch{");
        super.visitElement(section);
        tokens.add("}catch");
    }

    @Override
    public void visitWhileStatement(PsiWhileStatement statement) {
        tokens.add("while{");
        super.visitWhileStatement(statement);
        tokens.add("}while");
    }

    @Override
    public void visitDoWhileStatement(PsiDoWhileStatement statement) {
        tokens.add("doWhile{");
        super.visitDoWhileStatement(statement);
        tokens.add("}doWhile");
    }

    @Override
    public void visitForStatement(PsiForStatement statement) {
        tokens.add("for{");
        super.visitForStatement(statement);
        tokens.add("}for");
    }

    @Override
    public void visitForeachStatement(PsiForeachStatement statement) {
        tokens.add("forEach{");
        super.visitForeachStatement(statement);
        tokens.add("}forEach");
    }

    @Override
    public void visitIfStatement(PsiIfStatement statement) {
        if (statement.getThenBranch() != null) {
            tokens.add("if{");
            super.visitElement(statement.getThenBranch());
            tokens.add("}if");
        }

        if (statement.getElseBranch() != null) {
            tokens.add("else{");
            super.visitElement(statement.getElseBranch());
            tokens.add("}else");
        }
    }

    @Override
    public void visitAssignmentExpression(PsiAssignmentExpression assignmentExpression){
        tokens.add("assignment");
    }
}

//        PsiMethod method = expression.resolveMethod();
//
//        if (method != null) {
//
//            System.out.println("Method: " + expression.getText());
//            String s = method.getName();
//
//            if ("fail".equals(s)) {
//                tokens.add("fail");
//            } else if ("assertEquals".equals(s)) {
//                tokens.add("assertEquals");
//            } else if ("assertNotEquals".equals(s)) {
//                tokens.add("assertNotEquals");
//            } else if ("assertSame".equals(s)) {
//                tokens.add("assertSame");
//            } else if ("assertNotSame".equals(s)) {
//                tokens.add("assertNotSame");
//            } else if ("assertThat".equals(s)) {
//                tokens.add("assertThat");
//            } else if ("assertTrue".equals(s)) {
//                tokens.add("assertTrue");
//            } else if ("assertFalse".equals(s)) {
//                tokens.add("assertFalse");
//            } else if ("assertNotNull".equals(s)) {
//                tokens.add("assertNotNull");
//            } else if ("assertNull".equals(s)) {
//                tokens.add("assertNull");
//            } else if (s.contains("assert")){
//                tokens.add("assertion_NotJUnit");
//            }
//            else {
//                tokens.add("methodCall");
//            }
//        }

