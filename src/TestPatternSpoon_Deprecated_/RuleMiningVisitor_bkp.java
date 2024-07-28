package TestPatternSpoon_Deprecated_;

import com.intellij.psi.*;
import com.intellij.psi.javadoc.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Deprecated
public class RuleMiningVisitor_bkp {

    private List<char[]> GetSubSequences(char[] input) {

        List<char[]> result = new LinkedList<>();

        for (int start = 0; start < input.length; start++) {
            for (int end = start + 1; end <= input.length; end++) {
                result.add(Arrays.copyOfRange(input, start, end));
            }
        }

        return result;
    }

    public static String filePath
            = "/Users/wujianwei/Documents/ideaProjects1/FindNameLite/RuleMiningData/VisitorResults.txt";

    RuleMiningVisitor_bkp() {
    }

    public static void writeToFile(String data) throws IOException {

        Path path = Paths.get(filePath);
        byte[] data_bytes = data.getBytes();
        Files.write(path, data_bytes, StandardOpenOption.APPEND);
    }

    JavaRecursiveElementVisitor miningVisitor() {

        return new JavaRecursiveElementVisitor() {

            @Override
            public void visitAnonymousClass(PsiAnonymousClass aClass) {
                visitClass(aClass);
            }

            @Override
            public void visitArrayAccessExpression(PsiArrayAccessExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitArrayInitializerExpression(PsiArrayInitializerExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitAssertStatement(PsiAssertStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitAssignmentExpression(PsiAssignmentExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitBinaryExpression(PsiBinaryExpression expression) {
                visitPolyadicExpression(expression);
            }

            @Override
            public void visitBlockStatement(PsiBlockStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitBreakStatement(PsiBreakStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitClass(PsiClass aClass) {
                visitElement(aClass);
            }

            @Override
            public void visitClassInitializer(PsiClassInitializer initializer) {
                visitElement(initializer);
            }

            @Override
            public void visitClassObjectAccessExpression(PsiClassObjectAccessExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitConditionalExpression(PsiConditionalExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitContinueStatement(PsiContinueStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitDeclarationStatement(PsiDeclarationStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitDocComment(PsiDocComment comment) {
                visitComment(comment);
            }

            @Override
            public void visitDocTag(PsiDocTag tag) {
                visitElement(tag);
            }

            @Override
            public void visitDocTagValue(PsiDocTagValue value) {
                visitElement(value);
            }

            @Override
            public void visitDoWhileStatement(PsiDoWhileStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitEmptyStatement(PsiEmptyStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitExpression(PsiExpression expression) {
                visitElement(expression);
            }

            @Override
            public void visitExpressionList(PsiExpressionList list) {
                visitElement(list);
            }

            @Override
            public void visitExpressionListStatement(PsiExpressionListStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitExpressionStatement(PsiExpressionStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitField(PsiField field) {
                visitVariable(field);
            }

            @Override
            public void visitForStatement(PsiForStatement statement) {
                System.out.println("before For");
                super.visitForStatement(statement);
                System.out.println("after For");
            }

            @Override
            public void visitForeachStatement(PsiForeachStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitIdentifier(PsiIdentifier identifier) {
                visitJavaToken(identifier);
            }

            @Override
            public void visitIfStatement(PsiIfStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitImportList(PsiImportList list) {
                visitElement(list);
            }

            @Override
            public void visitImportStatement(PsiImportStatement statement) {
                visitElement(statement);
            }

            @Override
            public void visitImportStaticStatement(PsiImportStaticStatement statement) {
                visitElement(statement);
            }

            @Override
            public void visitInlineDocTag(PsiInlineDocTag tag) {
                visitDocTag(tag);
            }

            @Override
            public void visitInstanceOfExpression(PsiInstanceOfExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitJavaToken(PsiJavaToken token) {
                visitElement(token);
            }

            @Override
            public void visitKeyword(PsiKeyword keyword) {
                visitJavaToken(keyword);
            }

            @Override
            public void visitLabeledStatement(PsiLabeledStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitLiteralExpression(PsiLiteralExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitLocalVariable(PsiLocalVariable variable) {
                visitVariable(variable);
            }

            @Override
            public void visitMethod(PsiMethod method) {
                visitElement(method);
            }

            @Override
            public void visitCallExpression(PsiCallExpression callExpression) {
                visitExpression(callExpression);
            }

            @Override
            public void visitModifierList(PsiModifierList list) {
                visitElement(list);
            }

            @Override
            public void visitNewExpression(PsiNewExpression expression) {
                visitCallExpression(expression);
            }

            @Override
            public void visitPackage(PsiPackage aPackage) {
                visitElement(aPackage);
            }

            @Override
            public void visitPackageStatement(PsiPackageStatement statement) {
                visitElement(statement);
            }

            @Override
            public void visitParameter(PsiParameter parameter) {
                visitVariable(parameter);
            }

            @Override
            public void visitReceiverParameter(PsiReceiverParameter parameter) {
                visitVariable(parameter);
            }

            @Override
            public void visitParameterList(PsiParameterList list) {
                visitElement(list);
            }

            @Override
            public void visitParenthesizedExpression(PsiParenthesizedExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitUnaryExpression(PsiUnaryExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitPostfixExpression(PsiPostfixExpression expression) {
                visitUnaryExpression(expression);
            }

            @Override
            public void visitPrefixExpression(PsiPrefixExpression expression) {
                visitUnaryExpression(expression);
            }

            @Override
            public void visitReferenceElement(PsiJavaCodeReferenceElement reference) {
                visitElement(reference);
            }

            @Override
            public void visitImportStaticReferenceElement(PsiImportStaticReferenceElement reference) {
                visitElement(reference);
            }

            @Override
            public void visitReferenceExpression(PsiReferenceExpression expression) {
            }

            @Override
            public void visitMethodReferenceExpression(PsiMethodReferenceExpression expression) {
                visitReferenceExpression(expression);
            }

            @Override
            public void visitReferenceList(PsiReferenceList list) {
                visitElement(list);
            }

            @Override
            public void visitReferenceParameterList(PsiReferenceParameterList list) {
                visitElement(list);
            }

            @Override
            public void visitTypeParameterList(PsiTypeParameterList list) {
                visitElement(list);
            }

            @Override
            public void visitReturnStatement(PsiReturnStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitStatement(PsiStatement statement) {
                visitElement(statement);
            }

            @Override
            public void visitSuperExpression(PsiSuperExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitSwitchLabelStatement(PsiSwitchLabelStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitSwitchStatement(PsiSwitchStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitSynchronizedStatement(PsiSynchronizedStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitThisExpression(PsiThisExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitThrowStatement(PsiThrowStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitTryStatement(PsiTryStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitCatchSection(PsiCatchSection section) {
                visitElement(section);
            }

            @Override
            public void visitResourceList(PsiResourceList resourceList) {
                visitElement(resourceList);
            }

            @Override
            public void visitResourceVariable(PsiResourceVariable variable) {
                visitLocalVariable(variable);
            }

            @Override
            public void visitResourceExpression(PsiResourceExpression expression) {
                visitElement(expression);
            }

            @Override
            public void visitTypeElement(PsiTypeElement type) {
                visitElement(type);
            }

            @Override
            public void visitTypeCastExpression(PsiTypeCastExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitVariable(PsiVariable variable) {
                visitElement(variable);
            }

            @Override
            public void visitWhileStatement(PsiWhileStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitJavaFile(PsiJavaFile file) {
                visitFile(file);
            }

            @Override
            public void visitImplicitVariable(ImplicitVariable variable) {
                visitLocalVariable(variable);
            }

            @Override
            public void visitDocToken(PsiDocToken token) {
                visitElement(token);
            }

            @Override
            public void visitTypeParameter(PsiTypeParameter classParameter) {
                visitClass(classParameter);
            }

            @Override
            public void visitAnnotation(PsiAnnotation annotation) {
                visitElement(annotation);
            }

            @Override
            public void visitAnnotationParameterList(PsiAnnotationParameterList list) {
                visitElement(list);
            }

            @Override
            public void visitAnnotationArrayInitializer(PsiArrayInitializerMemberValue initializer) {
                visitElement(initializer);
            }

            @Override
            public void visitNameValuePair(PsiNameValuePair pair) {
                visitElement(pair);
            }

            @Override
            public void visitAnnotationMethod(PsiAnnotationMethod method) {
                visitMethod(method);
            }

            @Override
            public void visitEnumConstant(PsiEnumConstant enumConstant) {
                visitField(enumConstant);
            }

            @Override
            public void visitEnumConstantInitializer(PsiEnumConstantInitializer enumConstantInitializer) {
                visitAnonymousClass(enumConstantInitializer);
            }

            @Override
            public void visitCodeFragment(JavaCodeFragment codeFragment) {
                visitFile(codeFragment);
            }

            @Override
            public void visitPolyadicExpression(PsiPolyadicExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitLambdaExpression(PsiLambdaExpression expression) {
                visitExpression(expression);
            }

            @Override
            public void visitModule(PsiJavaModule module) {
                visitElement(module);
            }

            @Override
            public void visitModuleReferenceElement(PsiJavaModuleReferenceElement refElement) {
                visitElement(refElement);
            }

            @Override
            public void visitModuleStatement(PsiStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitRequiresStatement(PsiRequiresStatement statement) {
                visitModuleStatement(statement);
            }

            @Override
            public void visitPackageAccessibilityStatement(PsiPackageAccessibilityStatement statement) {
                visitModuleStatement(statement);
            }

            @Override
            public void visitUsesStatement(PsiUsesStatement statement) {
                visitStatement(statement);
            }

            @Override
            public void visitProvidesStatement(PsiProvidesStatement statement) {
                visitModuleStatement(statement);
            }

            @Override
            public void visitCodeBlock(PsiCodeBlock block) {

//                List<String> results = new LinkedList<>();

                try {
                    PsiStatement[] statements = block.getStatements();

                    String line = "";

                    for (PsiStatement statement : statements) {

                        if (statement instanceof PsiDeclarationStatement) {
                            line = line + " 1 " + "-1";
//                            writeToFile(" 1 " + "-1 ");
//                            results.add("1");
                        }

                        if (statement instanceof PsiExpressionStatement) {

                            PsiExpression expression = ((PsiExpressionStatement) statement).getExpression();

                            if (expression instanceof PsiMethodCallExpression) {

                                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expression;

                                if (methodCallExpression.getMethodExpression().getQualifiedName().startsWith("assert") ||
                                        methodCallExpression.getText().startsWith("Assert")) {

                                    line = line + " 2 " + "-1";
//                                    writeToFile(" 2 " + "-1 ");
//                                    results.add("2");
                                } else {
                                    line = line + " 3 " + "-1";
//                                    writeToFile(" 3 " + "-1 ");
//                                    results.add("3");
                                }
                            }
                        }

                        if (statement instanceof PsiAssertStatement) {
                            line = line + " 4 " + "-1";
//                            writeToFile(" 4 " + "-1 ");
//                            results.add("4");
                        }

                        if (statement instanceof PsiTryStatement) {
                            line = line + " 5 " + "-1";
//                            writeToFile(" 5 " + "-1 ");
//                            results.add("5");
                        }

                        if (statement instanceof PsiIfStatement) {
                            line = line + " 6 " + "-1";
//                            writeToFile(" 6 " + "-1 ");
//                            results.add("6");
                        }

                        if (statement instanceof PsiWhileStatement) {
                            line = line + " 7 " + "-1";
//                            writeToFile(" 7 " + "-1 ");
//                            results.add("7");
                        }

                        if (statement instanceof PsiForStatement) {
                            line = line + " 8 " + "-1";
//                            writeToFile(" 8 " + "-1 ");
//                            results.add("8");
                        }

                        if (statement instanceof PsiForeachStatement) {
                            line = line + " 9 " + "-1";
//                            writeToFile(" 9 " + "-1 ");
//                            results.add("9");
                        }

                        if (statement instanceof PsiDoWhileStatement) {
                            line = line + " 10 " + "-1";
//                            writeToFile(" 10 " + "-1 ");
//                            results.add("10");
                        }
                    }

                    line = line + " -2\n";
                    line = line.replaceFirst(" ", "");
                    writeToFile(line);
                    //Note: For sequential pattern mining

//                    writeToFile("-2\n");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            //Readme: Declaration - 1
            //Readme: Assertion - 2
            //Readme: MethodInvocation - 3
            //Readme: Assert - 4
            //Readme: TryCatch - 5
            //Readme: IfElse - 6
            //Readme: WhileLoop - 7
            //Readme: ForLoop - 8
            //Readme: ForEachLoop - 9
            //Readme: DoWhileLoop - 10

            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                System.out.println("MethodCall");
                super.visitMethodCallExpression(expression);
            }
        };
    }
}