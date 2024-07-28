package ExtrasForTestPattern;

import com.intellij.psi.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class NestedStatementsFilter_v1 {

    //Nested Statements Filter: to exclude all nested control-flow-based statements

    //Version 1 - If-Else statement, Try-Catch statement, Loop statement

    public static boolean checkIfElse(PsiIfStatement ifStatement){

        List<PsiStatement> all_statements =  new LinkedList<>();

        if (ifStatement.getThenBranch() == null && ifStatement.getElseBranch() == null) return false;

        if (ifStatement.getThenBranch() != null){

            Optional<PsiCodeBlock> codeBlock = Arrays.stream(ifStatement.getThenBranch().getChildren())
                    .filter(psiElement -> psiElement instanceof PsiCodeBlock)
                    .map(PsiCodeBlock.class::cast)
                    .findAny();

            if (!codeBlock.isPresent()) return false;

            all_statements.addAll(Arrays.asList(codeBlock.get().getStatements()));
        }

        if (ifStatement.getElseBranch() != null){

            Optional<PsiCodeBlock> codeBlock = Arrays.stream(ifStatement.getElseBranch().getChildren())
                    .filter(psiElement -> psiElement instanceof PsiCodeBlock)
                    .map(PsiCodeBlock.class::cast)
                    .findAny();

            if (!codeBlock.isPresent()) return false;

            all_statements.addAll(Arrays.asList(codeBlock.get().getStatements()));
        }

        List<PsiStatement> statements1 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiTryStatement)
                .collect(Collectors.toList());

        List<PsiStatement> statements2 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiIfStatement)
                .collect(Collectors.toList());

        List<PsiStatement> statements3 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiLoopStatement)
                .collect(Collectors.toList());

        return statements1.size() != 0 || statements2.size() != 0 || statements3.size() != 0;
    }

    public static boolean checkTryCatch(PsiTryStatement tryCatchStatement){

        List<PsiStatement> all_statements =  new LinkedList<>();

        if (tryCatchStatement.getTryBlock() == null
                && tryCatchStatement.getCatchBlocks().length == 0
                && tryCatchStatement.getFinallyBlock() == null) return false;

        if (tryCatchStatement.getTryBlock() != null){

            all_statements.addAll(Arrays.asList(tryCatchStatement.getTryBlock().getStatements()));
        }

        if (tryCatchStatement.getCatchBlocks().length != 0){

            for (PsiCodeBlock codeBlock : tryCatchStatement.getCatchBlocks()){

                all_statements.addAll(Arrays.asList(codeBlock.getStatements()));
            }
        }

        if (tryCatchStatement.getFinallyBlock() != null){

            all_statements.addAll(Arrays.asList(tryCatchStatement.getFinallyBlock().getStatements()));
        }

        List<PsiStatement> statements1 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiTryStatement)
                .collect(Collectors.toList());

        List<PsiStatement> statements2 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiIfStatement)
                .collect(Collectors.toList());

        List<PsiStatement> statements3 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiLoopStatement)
                .collect(Collectors.toList());

        return statements1.size() != 0 || statements2.size() != 0 || statements3.size() != 0;
    }

    public static boolean checkLoop (PsiLoopStatement loopStatement){

        if (loopStatement.getBody() == null) return false;

        Optional<PsiCodeBlock> codeBlock = Arrays.stream(loopStatement.getBody().getChildren())
                .filter(psiElement -> psiElement instanceof PsiCodeBlock)
                .map(PsiCodeBlock.class::cast)
                .findAny();

        if (!codeBlock.isPresent()) return false;

        List<PsiStatement> all_statements =
                new LinkedList<>(Arrays.asList(codeBlock.get().getStatements()));

        List<PsiStatement> statements1 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiTryStatement)
                .collect(Collectors.toList());

        List<PsiStatement> statements2 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiIfStatement)
                .collect(Collectors.toList());

        List<PsiStatement> statements3 =  all_statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiLoopStatement)
                .collect(Collectors.toList());

        return statements1.size() != 0 || statements2.size() != 0 || statements3.size() != 0;
    }
}
