package ExtrasForTestPattern;

import com.intellij.psi.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FailDetector_v1 {

    //Version 1 -> Try-Catch, If-Else, Loop

    public boolean detectFailStatement(PsiCodeBlock testBody){

        for (PsiStatement statement : testBody.getStatements()){

            if (statement instanceof PsiExpressionStatement){

                PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;

                if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) continue;

                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

                String name = methodCallExpression.getMethodExpression().getReferenceName();

                if (name == null) continue;

                if (name.equals("fail")) return true;
            }

            if (statement instanceof PsiTryStatement){

                PsiTryStatement tryStatement = (PsiTryStatement) statement;

                PsiCodeBlock try_block = tryStatement.getTryBlock();

                PsiCodeBlock[] catch_blocks = tryStatement.getCatchBlocks();

                List<PsiStatement> try_statements = new LinkedList<>();

                List<PsiStatement> catch_statements = new LinkedList<>();

                if (try_block != null) {

                    try_statements.addAll(Arrays.asList(try_block.getStatements()));
                }

                if (catch_blocks.length != 0) {

                    for (PsiCodeBlock codeBlock : catch_blocks){

                        if (codeBlock == null) continue;

                        catch_statements.addAll(Arrays.asList(codeBlock.getStatements()));
                    }
                }

                for (PsiStatement statement1 : try_statements){

                    if (statement1 instanceof PsiExpressionStatement){

                        PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement1;

                        if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) continue;

                        PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

                        String name = methodCallExpression.getMethodExpression().getReferenceName();

                        if (name == null) continue;

                        if (name.equals("fail")) return true;
                    }
                }

                for (PsiStatement statement1 : catch_statements){

                    if (statement1 instanceof PsiExpressionStatement){

                        PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement1;

                        if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) continue;

                        PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

                        String name = methodCallExpression.getMethodExpression().getReferenceName();

                        if (name == null) continue;

                        if (name.equals("fail")) return true;
                    }
                }
            }
            //Try-Catch Statement

            if (statement instanceof PsiIfStatement){

                PsiIfStatement ifStatement = (PsiIfStatement) statement;

                List<PsiStatement> if_innerStatements = new LinkedList<>();

                List<PsiStatement> else_innerStatements = new LinkedList<>();

                if (ifStatement.getThenBranch() instanceof PsiBlockStatement){

                    PsiBlockStatement if_block = (PsiBlockStatement) ifStatement.getThenBranch();

                    if (if_block.getCodeBlock().getStatements().length != 0){

                        if_innerStatements.addAll(Arrays.asList(if_block.getCodeBlock().getStatements()));
                    }
                }

                if (ifStatement.getElseBranch() instanceof PsiBlockStatement){

                    PsiBlockStatement else_block = (PsiBlockStatement) ifStatement.getElseBranch();

                    if (else_block.getCodeBlock().getStatements().length != 0){

                        else_innerStatements.addAll(Arrays.asList(else_block.getCodeBlock().getStatements()));
                    }
                }

                for (PsiStatement statement1 : if_innerStatements){

                    if (statement1 instanceof PsiExpressionStatement){

                        PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement1;

                        if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) continue;

                        PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

                        String name = methodCallExpression.getMethodExpression().getReferenceName();

                        if (name == null) continue;

                        if (name.equals("fail")) return true;
                    }
                }

                for (PsiStatement statement1 : else_innerStatements){

                    if (statement1 instanceof PsiExpressionStatement){

                        PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement1;

                        if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) continue;

                        PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

                        String name = methodCallExpression.getMethodExpression().getReferenceName();

                        if (name == null) continue;

                        if (name.equals("fail")) return true;
                    }
                }
            }
            //If-Else Statement

            if (statement instanceof PsiLoopStatement){

                PsiLoopStatement loopStatement = (PsiLoopStatement) statement;

                List<PsiStatement> loop_innerStatements = new LinkedList<>();

                if (loopStatement.getBody() instanceof PsiBlockStatement){

                    PsiBlockStatement blockStatement = (PsiBlockStatement) loopStatement.getBody();

                    if (blockStatement.getCodeBlock().getStatements().length != 0){

                        loop_innerStatements.addAll(Arrays.asList(blockStatement.getCodeBlock().getStatements()));
                    }
                }
                for (PsiStatement statement1 : loop_innerStatements){

                    if (statement1 instanceof PsiExpressionStatement){

                        PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement1;

                        if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) continue;

                        PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

                        String name = methodCallExpression.getMethodExpression().getReferenceName();

                        if (name == null) continue;

                        if (name.equals("fail")) return true;
                    }
                }
            }
            //Loop Statement
        }
        return false;
    }
}
