//
//package TestPatternSpoon_Deprecated_;
//
//@Deprecated
//
//public class SpoonPatternFinder {
//    //    Past codes:
////
////    List<CtMethod> fetchAllMatchedCases(String test_class);
////
////    String getCoreAction(String test_name, String test_class);
////
////    Set<String> getCorePredicate(String test_name, String test_class);
////
////    String getCoreScenario(String test_name, String test_class);
////
////    List<String> getAllPossibleActions(String test_name, String test_class);
////
////    List<String> getAllPossiblePredicates(String test_name, String test_class);
////
////    List<String> getAllPossibleScenarios(String test_name, String test_class);
//
//    //For PatternFiner.java
//
////    //private CtMethod<?> all_asserts_model = new AllAssertionsPattern().fetchModel();
////
////    private Project cur_project = ProjectManager.getInstance().getOpenProjects()[0];
////
////    private String core_action = "";
////
////    private Set<String> core_predicate = new LinkedHashSet<>();
////
////    private String core_scenario = "";
////
////    private void reportNull(){
////        System.out.println("Project is Null!");
////    }
////
////    @Override
////    public List<CtMethod> fetchAllMatchedCases(String test_class) {
////
////        List<CtMethod> results = new LinkedList<>();
////
////        String current_project = cur_project.getBasePath();
////
////        Launcher launcher1 = new Launcher();
////
////        if (current_project != null) {
////            launcher1.addInputResource(current_project);
////        }
////        else{
////            reportNull();
////        }
////
////        launcher1.buildModel();
////
////        CtModel model1 = launcher1.getModel();
////
////        //Build the model for the tested program
////
////        for(CtPackage p : model1.getAllPackages()) {
////
////            for (CtType<?> type : p.getTypes()){
////
////                if (type.isClass()
////                        && type.isPublic()){
////                    for (CtMethod<?> method : type.getMethods()){
////
////                        if (method.getAnnotation(Test.class) == null) {
////                            continue;
////                        }
////                        //Exclude other methods
////
////                        List<CtStatement> all_statements =
////                                method.getBody().getStatements();
////
////                        List<CtInvocation> all_invocations
////                                = method.getBody().getStatements().stream()
////                                .filter(CtInvocation.class::isInstance)
////                                .filter(invocation -> invocation.toString().contains("assert"))
////                                .map(CtInvocation.class::cast)
////                                .collect(Collectors.toList());
////
////
////                        if (all_statements.size() != all_invocations.size()) continue;
////
////                        CtInvocation<?> first_assert = all_invocations.get(0);
////
////                        List<CtExecutableReferenceImpl> inside_first_assert = first_assert
////                                .filterChildren(new TypeFilter<>(CtInvocation.class)).
////                                        map((CtFunction<CtInvocation, CtExecutableReference>)
////                                                CtAbstractInvocation::getExecutable).list();
////
////
////                        if (inside_first_assert.size() < 3) continue;
////
////                        List<CtInvocation> sub_statement = first_assert.filterChildren(
////                                new TypeFilter<>(CtInvocation.class)).list();
////
////                        if (!sub_statement.get(1).getTarget().toString()
////                                .equals(sub_statement.get(2).getTarget().toString())) continue;
////
////                        //The first and second targets must be the same to be the scenario
////                        //Use the first assertion's first method call as the focal method of the test
////                        //Use the second invocation as predicate
////                        //Use the first target in the first assertion as the scenario
////
////                        results.add(method);
////
////                    }
////                }
////            }
////        }
////        return results;
////    }
////
////
////    @Override
////    public String getCoreAction(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                List<CtInvocation> all_invocations
////                        = method.getBody().getStatements().stream()
////                        .filter(CtInvocation.class::isInstance)
////                        .filter(invocation -> invocation.toString().contains("assert"))
////                        .map(CtInvocation.class::cast)
////                        .collect(Collectors.toList());
////
////                CtInvocation<?> first_assert = all_invocations.get(0);
////
////                List<CtExecutableReferenceImpl> inside_first_assert = first_assert
////                        .filterChildren(new TypeFilter<>(CtInvocation.class)).
////                                map((CtFunction<CtInvocation, CtExecutableReference>)
////                                        CtAbstractInvocation::getExecutable).list();
////
////                core_action = inside_first_assert.get(1).getSimpleName();
////
////                return core_action;
////            }
////        }
////
////        return "Core Action Missing!";
////    }
////
////    @Override
////    public Set<String> getCorePredicate(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for (CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                List<CtInvocation> all_invocations
////                        = method.getBody().getStatements().stream()
////                        .filter(CtInvocation.class::isInstance)
////                        .filter(invocation -> invocation.toString().contains("assert"))
////                        .map(CtInvocation.class::cast)
////                        .collect(Collectors.toList());
////
////                CtInvocation<?> first_assert = all_invocations.get(0);
////
////                List<CtExecutableReferenceImpl> inside_first_assert = first_assert
////                        .filterChildren(new TypeFilter<>(CtInvocation.class)).
////                                map((CtFunction<CtInvocation, CtExecutableReference>)
////                                        CtAbstractInvocation::getExecutable).list();
////
////                if (core_predicate.size() == 0) {
////                    core_predicate.add(inside_first_assert.get(2).getSimpleName());
////                }
////
////                return core_predicate;
////            }
////        }
////
////        return null;
////    }
////
////    @Override
////    public String getCoreScenario(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for (CtMethod method : all_matched_test) {
////
////            if (method.getSimpleName().equals(test_name)){
////
////                List<CtInvocation> all_invocations
////                        = method.getBody().getStatements().stream()
////                        .filter(CtInvocation.class::isInstance)
////                        .filter(invocation -> invocation.toString().contains("assert"))
////                        .map(CtInvocation.class::cast)
////                        .collect(Collectors.toList());
////
////                CtInvocation<?> first_assert = all_invocations.get(0);
////
////                List<CtInvocation> sub_statement = first_assert.filterChildren(
////                        new TypeFilter<>(CtInvocation.class)).list();
////
////                core_scenario = sub_statement.get(1).getTarget().toString();
////
////                return core_scenario;
////
////            }
////        }
////
////        return "Core Scenario Missing!";
////    }
////
////
////
////
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleActions(String test_name, String test_class) {
////
//////        List<String> actions = new LinkedList<>();
//////
//////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
//////
//////        for (CtMethod method : all_matched_test){
//////
//////            if (method.getSimpleName().equals(test_name)){
//////
//////                List<CtExecutableReferenceImpl> cur_invocations =
//////                        method.filterChildren(new TypeFilter<>(CtInvocation.class)).
//////                                map((CtFunction<CtInvocation, CtExecutableReference>)
//////                                        CtAbstractInvocation::getExecutable).list();
//////
//////                for (CtExecutableReference reference : cur_invocations){
//////
//////                    if (reference.getSimpleName().startsWith("assert")
//////                            ||reference.getSimpleName().startsWith("Assert")) continue;
//////
//////                    actions.add(reference.getSimpleName());
//////                }
//////
//////
//////            }
//////        }
////
////        return null;
////    }
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossiblePredicates(String test_name, String test_class) {
////
//////        List<String> predicates = new LinkedList<>();
//////
//////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
//////
//////        for (CtMethod method : all_matched_test){
//////
//////            if (method.getSimpleName().equals(test_name)){
//////
//////                List<CtExecutableReferenceImpl> cur_invocations =
//////                        method.filterChildren(new TypeFilter<>(CtInvocation.class)).
//////                                map((CtFunction<CtInvocation, CtExecutableReference>)
//////                                        CtAbstractInvocation::getExecutable).list();
//////
//////                for (CtExecutableReference reference : cur_invocations){
//////
//////                    if (reference.getSimpleName().startsWith("assert")
//////                            ||reference.getSimpleName().startsWith("Assert")){
//////
//////                        predicates.add(reference.getSimpleName());
//////                    }
//////
//////                }
//////            }
//////        }
////
////        return null;
////    }
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleScenarios(String test_name, String test_class) {
////
//////        List<String> scenarios = new LinkedList<>();
//////
//////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
//////
//////        for (CtMethod method : all_matched_test){
//////
//////            if (method.getSimpleName().equals(test_name)){
//////
//////                CtBlock block = method.getBody();
//////
//////                for (CtStatement statement : block.getStatements()){
//////
//////                    List<CtElement> list_embedded_variable = statement.filterChildren
//////                            (new TypeFilter<CtElement>(CtElement.class){
//////                                @Override
//////                                public boolean matches(CtElement element){
//////                                    return super.matches(element) &&
//////                                            (element.getClass().equals(CtFieldReferenceImpl.class)
//////                                            || element.getClass().equals(CtConstructorCallImpl.class));
//////                                }
//////                            }).list();
//////                    //Find the embedded field references and constructor calls
//////
////////                    for (CtElement element : list_embedded_variable){
////////
////////                        System.out.println("Ele: " + element.toString() + " - " + element.getClass().toString());
////////                    }
//////
//////                    List<String> all_new_objects = new LinkedList<>();
//////
//////                    for (CtElement element:
//////                         list_embedded_variable) {
//////
//////                        if (element.getClass().equals(CtConstructorCallImpl.class)){
//////
//////                            CtConstructorCall cur_ele = (CtConstructorCall) element;
//////
//////                            all_new_objects.add(cur_ele.getType().getSimpleName());
//////                        }
//////
//////                        else if (element.getClass().equals(CtFieldReferenceImpl.class)){
//////
//////                            CtFieldReferenceImpl cur_ele = (CtFieldReferenceImpl) element;
//////
//////                            all_new_objects.add(cur_ele.getSimpleName());
//////                        }
//////
//////
//////                    }
//////
//////
//////                    scenarios.addAll(all_new_objects);
//////
//////
////////                    System.out.println("\n");
//////                }
//////            }
//////        }
////
////        return null;
////    }
//
//    //Todo: AllAssertion
//
////    private String core_action = "";
////
////    private Set<String> core_predicate = new LinkedHashSet<>();
////
////    private String core_scenario = "";
////
////    private Project cur_project = ProjectManager.getInstance().getOpenProjects()[0];
////
////    private void reportNull(){
////        System.out.println("Project is Null!");
////    }
////
////    @Override
////    public List<CtMethod> fetchAllMatchedCases(String test_class) {
////
////        String current_project = cur_project.getBasePath();
////
////        Launcher launcher1 = new Launcher();
////
////        List<CtMethod> results = new LinkedList<>();
////
////        if (current_project != null) {
////            launcher1.addInputResource(current_project);
////        }
////        else{
////            reportNull();
////        }
////        launcher1.buildModel();
////
////        CtModel model1 = launcher1.getModel();
////
////        //Build the model for the tested program
////
////        for(CtPackage p : model1.getAllPackages()) {
////
////            for (CtType<?> type : p.getTypes()){
////
////                if (type.isClass()
////                        && type.isPublic()
////                        && (type.getSimpleName().contains("test") || type.getSimpleName().contains("Test"))){
////
////                    for (CtMethod<?> method : type.getMethods()){
////
////                        if (method.getAnnotation(Test.class) == null) continue;
////
////                        CtStatement first_line = method.getBody().getStatement(0);
////
////                        if (!(first_line instanceof CtLocalVariable)) continue;
////                        //First Line must be a local variable as the scenario
////
////                        CtStatement second_line = method.getBody().getStatement(1);
////
////                        if (!(second_line instanceof CtIf)) continue;
////                        //First Line must be the if - else statement
////
////                        List<CtIf> ifStatements = method.getBody().getStatements().stream()
////                                .filter(CtIf.class::isInstance)
////                                .map(CtIf.class::cast)
////                                .collect(Collectors.toList());
////
////                        if (ifStatements.size() != 1) continue;
////
////                        //Fetch All If-Else in the test body and should be one if-else statement
////
////                        //Exclude all over-sized if-else statement
////
////                        CtIf first_if = ifStatements.get(0);
////
////                        CtStatement if_body = first_if.getThenStatement();
////
////                        CtStatement else_body = first_if.getElseStatement();
////
////                        CtExpression condition = first_if.getCondition();
////
////                        //Get if and else body
////
////                        List<CtInvocation> if_invocations =
////                                if_body.filterChildren(new TypeFilter<>(CtInvocation.class)).list();
////
////                        List<CtInvocation> else_invocations =
////                                else_body.filterChildren(new TypeFilter<>(CtInvocation.class)).list();
////
////                        if (if_invocations.size() > 3 || else_invocations.size() > 3) continue;
////
////                        if (if_invocations.size() == 0 || else_invocations.size() == 0) continue;
////
////                        if (!condition.toString().
////                                contains(((CtLocalVariable) first_line).getSimpleName())) continue;
////
////                        //Exclude if-else statement with more than 3 invocations in if or else
////
////                        results.add(method);
////                    }
////                }
////            }
////        }
////
////        return results;
////    }
////
////    @Override
////    public String getCoreAction(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        AllLoop:
////        for (CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtIf first_if = (CtIf) method.getBody().getStatements().get(1);
////
////                CtStatement if_body = first_if.getThenStatement();
////
////                List<CtInvocation> invocations = if_body.
////                        filterChildren(new TypeFilter<>(CtInvocation.class)).list();
////
////                for (CtInvocation invocation : invocations){
////
////                    if (invocation.getExecutable().getSimpleName().contains("assert")) continue;
////
////                    core_action = invocation.getExecutable().getSimpleName();
////
////                    break AllLoop;
////                }
////            }
////        }
////
////        return core_action;
////    }
////
////    @Override
////    public Set<String> getCorePredicate(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for (CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtIf first_if = (CtIf) method.getBody().getStatements().get(1);
////
////                CtStatement else_body = first_if.getElseStatement();
////
////                List<CtInvocation> invocations = else_body.
////                        filterChildren(new TypeFilter<>(CtInvocation.class)).list();
////
////                for (CtInvocation invocation : invocations){
////
////                    if (invocation.getExecutable().getSimpleName().contains("assert")) continue;
////
////                    core_predicate.add(invocation.getExecutable().getSimpleName());
////                }
////
////            }
////        }
////
////        return core_predicate;
////    }
////
////    @Override
////    public String getCoreScenario(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for (CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtStatement first_line = method.getBody().getStatement(0);
////
////                CtLocalVariable<?> first = (CtLocalVariable<?>) first_line;
////
////                core_scenario = first.getSimpleName();
////            }
////        }
////
////        return core_scenario;
////    }
////
////
////
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleActions(String test_name, String test_class) {
////
////        List<String> actions = new LinkedList<>();
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for (CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                List<CtExecutableReferenceImpl> cur_invocations =
////                        method.filterChildren(new TypeFilter<>(CtInvocation.class)).
////                                map((CtFunction<CtInvocation, CtExecutableReference>)
////                                        CtAbstractInvocation::getExecutable).list();
////
////                //System.out.println("All invocations: " + ArrayUtils.toString(cur_invocations));
////
////                CtBlock cur_block = method.getBody();
////
////                CtIf if_else_statement = null;
////
////                for (CtStatement statement:
////                        cur_block.getStatements()) {
////
////                    if (statement.getClass().equals(CtIfImpl.class)){
////
////                        if_else_statement = (CtIf) statement;
////
////                        break;
////
////                    }
////                }
////                //Get the first try-catch statement's catcher part
////
////                assert if_else_statement != null;
////
////                //String if_part = if_else_statement.getThenStatement().toString();
////
////                List<CtExecutableReferenceImpl> invocations_if_part =
////                        if_else_statement.getThenStatement().filterChildren(new TypeFilter<>(CtInvocation.class)).
////                                map((CtFunction<CtInvocation, CtExecutableReference>)
////                                        CtAbstractInvocation::getExecutable).list();
////
////                for (CtExecutableReference invocations:
////                        invocations_if_part) {
////
////                    if (cur_invocations.contains(invocations)){
////
////                        actions.add(invocations.getSimpleName());
////                    }
////
////                }
////
////            }
////        }
////
////        return actions;
////    }
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossiblePredicates(String test_name, String test_class) {
////
////        List<String> predicates = new LinkedList<>();
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for (CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                List<CtExecutableReferenceImpl> cur_invocations =
////                        method.filterChildren(new TypeFilter<>(CtInvocation.class)).
////                                map((CtFunction<CtInvocation, CtExecutableReference>)
////                                        CtAbstractInvocation::getExecutable).list();
////
////                //System.out.println("All invocations: " + ArrayUtils.toString(cur_invocations));
////
////                CtBlock cur_block = method.getBody();
////
////                CtIf if_else_statement = null;
////
////                for (CtStatement statement:
////                        cur_block.getStatements()) {
////
////                    if (statement.getClass().equals(CtIfImpl.class)){
////
////                        if_else_statement = (CtIf) statement;
////
////                        break;
////
////                    }
////                }
////                //Get the first try-catch statement's catcher part
////
////                assert if_else_statement != null;
////
////                //String else_part = if_else_statement.getElseStatement().toString();
////
////                List<CtExecutableReferenceImpl> invocations_else_part =
////                        if_else_statement.getElseStatement().filterChildren(new TypeFilter<>(CtInvocation.class)).
////                        map((CtFunction<CtInvocation, CtExecutableReference>)
////                                CtAbstractInvocation::getExecutable).list();
////
////                for (CtExecutableReference reference : invocations_else_part){
////
////                    if (cur_invocations.contains(reference)){
////
////                        predicates.add(reference.getSimpleName());
////                    }
////                }
////            }
////        }
////
////        return predicates;
////    }
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleScenarios(String test_name, String test_class) {
////
////        List<String> scenarios = new LinkedList<>();
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        CtIf if_else_statement = null;
////
////        for (CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtBlock cur_block = method.getBody();
////
////                for (CtStatement statement:
////                        cur_block.getStatements()) {
////
////                    if (statement.getClass().equals(CtIfImpl.class)){
////
////                        if_else_statement = (CtIf) statement;
////
////                    }
////
////                }
////                //Get the if else statement
////            }
////        }
////
////        for (CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtBlock cur_block = method.getBody();
////
////                for (CtStatement statement:
////                        cur_block.getStatements()) {
////
//////                    List<CtElement> list_embedded_variable = statement.filterChildren
//////                            (new TypeFilter<CtElement>(CtElement.class){
//////                                public boolean matches(CtElement element){
//////                                    return super.matches(element) && element.getClass().equals(CtLocalVariableImpl.class);
//////                                }
//////                            }).list();
//////                    System.out.println("All elements in statment: " +ArrayUtils.toString(list_embedded_variable));
//////                    for (CtElement element : list_embedded_variable){
//////                        System.out.println("Element: " + element.toString()
//////                        +"\n" + "ele class: " + element.getClass().toString());
//////                    }
////
////
////                    CtLocalVariable<?> localVariable;
////
////                    if (statement.getClass().equals(CtLocalVariableImpl.class)){
////
////                        localVariable = (CtLocalVariable) statement;
////
//////                        System.out.println("localVariable: " + localVariable.getSimpleName());
////
////                        assert if_else_statement != null;
////
////                        if (if_else_statement.getCondition().toString().contains(localVariable.getSimpleName())){
//////                            System.out.println("\n" + "Condition: " + if_else_statement.getCondition().toString());
//////                            System.out.println("Match in if else: " + localVariable.getSimpleName());
////
////                            scenarios.add(localVariable.getSimpleName());
////                        }
////                    }
////
////                }
////                //Get all matched objects (within the condition)
////            }
////        }
////        return scenarios;
////    }
//
//
//    //Todo: IfElse
//
////    private String core_action = "";
////
////    private Set<String> core_predicate = new LinkedHashSet<>();
////
////    private String core_scenario = "";
////
////    private Project cur_project = ProjectManager.getInstance().getOpenProjects()[0];
////
////    private void reportNull(){
////        System.out.println("Project is Null!");
////    }
////    @Override
////    public List<CtMethod> fetchAllMatchedCases(String test_class) {
////
////        String current_project = cur_project.getBasePath();
////
////        Launcher launcher1 = new Launcher();
////
////        List<CtMethod> results = new LinkedList<>();
////
////        if (current_project !=  null){
////
////            launcher1.addInputResource(current_project);
////        }
////        else {
////
////            reportNull();
////        }
////
////        launcher1.buildModel();
////
////        CtModel ctModel1 = launcher1.getModel();
////
////        //Build the model for the tested program
////
////
////        for (CtPackage ctPackage : ctModel1.getAllPackages()){
////
////            for (CtType<?> ctType : ctPackage.getTypes()){
////
////                if (ctType.isPublic()
////                        && ctType.isPublic()) {
////
////                    for (CtMethod<?> method : ctType.getMethods()) {
////
////                        if (method.getAnnotation(Test.class) == null) continue;
////
////                        CtStatement first_line = method.getBody().getStatement(0);
////
////                        if (!(first_line instanceof CtLoop)) continue;
////
////                        List<CtLoop> loops = method.getBody().getStatements().stream()
////                                .filter(CtLoop.class::isInstance)
////                                .map(CtLoop.class::cast)
////                                .collect(Collectors.toList());
////
////                        //Fetch All Loops in the test body
////
////                        if (loops.size() != 1) continue;
////
////                        CtStatement loop_body = loops.get(0).getBody();
////
////                        List<CtStatement> statements = loop_body
////                                .filterChildren(new TypeFilter<>(CtStatement.class)).list();
////
////                        statements.remove(loop_body);
////
////                        //Remove self
////
////                        CtStatement first = statements.get(0);
////
////                        CtStatement second = statements.get(1);
////
////                        if (!(first instanceof CtInvocation) && !(second instanceof CtInvocation)) continue;
////
////                        //CtInvocation<?> first_invocation = (CtInvocation) first; -> action
////
////                        CtInvocation<?> second_invocation = (CtInvocation) second;
////
////                        if (!second_invocation.getExecutable().getSimpleName().contains("assert")) continue;
////
////                        results.add(method);
////                    }
////                }
////            }
////        }
////
////        return results;
////    }
////
////    @Override
////    public String getCoreAction(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test =
////                this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtLoop loop = method.getBody().getStatement(0);
////
////                CtStatement loop_body = loop.getBody();
////
////                List<CtStatement> statements = loop_body
////                        .filterChildren(new TypeFilter<>(CtStatement.class)).list();
////
////                statements.remove(loop_body);
////
////                core_action = ((CtInvocation) statements.get(0)).getExecutable().getSimpleName();
////            }
////        }
////
////        return core_action;
////    }
////
////    @Override
////    public Set<String> getCorePredicate(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test =
////                this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtLoop loop = method.getBody().getStatement(0);
////
////                CtStatement loop_body = loop.getBody();
////
////                List<CtStatement> statements = loop_body
////                        .filterChildren(new TypeFilter<>(CtStatement.class)).list();
////
////                statements.remove(loop_body);
////
////                CtInvocation<?> second_line = (CtInvocation<?>) statements.get(1);
////
////                List<CtInvocation> invocationList =
////                        second_line.filterChildren(new TypeFilter<>(CtInvocation.class)).list();
////
////                for (CtInvocation invocation : invocationList){
////
////                    if (invocation.getExecutable().getSimpleName().contains("assert")) continue;
////
////                    core_predicate.add(invocation.getExecutable().getSimpleName());
////                }
////            }
////        }
////
////        return core_predicate;
////    }
////
////    @Override
////    public String getCoreScenario(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test =
////                this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                List<CtLoop> loops = method.getBody().getStatements().stream()
////                        .filter(CtLoop.class::isInstance)
////                        .map(CtLoop.class::cast)
////                        .collect(Collectors.toList());
////
////                CtLoop cur_loop = loops.get(0);
////
////                if (cur_loop instanceof CtForEach){
////
////                    CtForEach loop = (CtForEach) cur_loop;
////
////                    core_scenario = loop.getVariable().getSimpleName();
////                }
////
////                else if (cur_loop instanceof CtWhile){
////
////                    CtWhile loop = (CtWhile) cur_loop;
////
////                    List<CtFieldReference> variables =
////                            loop.getLoopingExpression().getElements(new TypeFilter<>(CtFieldReference.class));
////
////                    core_scenario = variables.get(0).getSimpleName();
////                }
////
////                else if (cur_loop instanceof CtDo){
////
////                    CtDo loop = (CtDo) cur_loop;
////
////                    List<CtFieldReference> variables =
////                            loop.getLoopingExpression().getElements(new TypeFilter<>(CtFieldReference.class));
////
////                    core_scenario = variables.get(0).getSimpleName();
////                }
////
////                else if (cur_loop instanceof CtFor){
////
////                    CtFor loop = (CtFor) cur_loop;
////
////                    List<CtFieldReference> variables =
////                            loop.getExpression().getElements(new TypeFilter<>(CtFieldReference.class));
////
////                    core_scenario = variables.get(0).getSimpleName();
////                }
////            }
////        }
////
////        return core_scenario;
////    }
//
//
//    //Todo: Loop
//
//    //    private String core_action = "";
////
////    private Set<String> core_predicate = new LinkedHashSet<>();
////
////    private String core_scenario = "";
////
////    private Project cur_project = ProjectManager.getInstance().getOpenProjects()[0];
////
////    private void reportNull(){
////        System.out.println("Project is Null!");
////    }
////
////    @Override
////    public List<CtMethod> fetchAllMatchedCases(String test_class) {
////        String current_project = cur_project.getBasePath();
////
////        Launcher launcher1 = new Launcher();
////
////        List<CtMethod> results = new LinkedList<>();
////
////        if (current_project !=  null){
////
////            launcher1.addInputResource(current_project);
////        }
////        else {
////
////            reportNull();
////        }
////
////        launcher1.buildModel();
////
////        CtModel ctModel1 = launcher1.getModel();
////
////        //Build the model for the tested program
////
////
////        for (CtPackage ctPackage : ctModel1.getAllPackages()){
////
////            for (CtType<?> ctType : ctPackage.getTypes()){
////
////                if (ctType.isPublic()
////                        && ctType.isPublic()) {
////
////                    for (CtMethod<?> method : ctType.getMethods()) {
////
////                        if (method.getAnnotation(Test.class) == null) continue;
////
////                        List<CtStatement> body = method.getBody().getStatements();
////
////                        if (body.size() <= 1) continue;
////
////                        List<CtInvocation> invocationList1 =
////                                method.filterChildren(new TypeFilter<>(CtInvocation.class)).list();
////
////                        List<CtInvocation> invocationList2 = invocationList1.stream()
////                                .filter(p -> !p.toString().contains("assert"))
////                                .collect(Collectors.toList());
////
////                        if (invocationList1.size() != invocationList2.size()) continue;
////
////                        CtStatement first_line = method.getBody().getStatement(0);
////
////                        CtStatement second_line = method.getBody().getStatement(1);
////
////                        if ((first_line instanceof CtLocalVariable
////                                && !(second_line instanceof CtInvocation))
////                                ||
////                                (first_line instanceof CtInvocation
////                                        && !(second_line instanceof CtLocalVariable))) continue;
////
////                        //Preset the structure of the test pattern
////
////                        //First line is the instantiation of the scenario / action
////
////                        //Second line is the action / instantiation of the scenario
////
////                        results.add(method);
////                    }
////                }
////            }
////
////        }
////        return results;
////    }
////
////    @Override
////    public String getCoreAction(String test_name, String test_class) {
////        List<CtMethod> all_matched_test =
////                this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtStatement first_line = method.getBody().getStatement(0);
////
////                CtStatement second_line = method.getBody().getStatement(1);
////
////
////                if (first_line instanceof CtInvocation){
////
////                    core_action = ((CtInvocation) first_line).getExecutable().getSimpleName();
////
////                }
////
////                else if (second_line instanceof CtInvocation){
////
////                    core_action = ((CtInvocation) second_line).getExecutable().getSimpleName();
////
////                }
////
////            }
////        }
////
////        return core_action;
////    }
////
////    @Override
////    public Set<String> getCorePredicate(String test_name, String test_class) {
////
////        return null;
////    }
////
////    @Override
////    public String getCoreScenario(String test_name, String test_class) {
////        List<CtMethod> all_matched_test =
////                this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtStatement first_line = method.getBody().getStatement(0);
////
////                CtStatement second_line = method.getBody().getStatement(1);
////
////
////                if (first_line instanceof CtLocalVariable){
////
////                    core_scenario = ((CtLocalVariable) first_line).getSimpleName();
////
////                }
////
////                else if (second_line instanceof CtLocalVariable){
////
////                    core_scenario = ((CtLocalVariable) second_line).getSimpleName();
////
////                }
////            }
////        }
////
////        return core_scenario;
////    }
////
////
////
////
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleActions(String test_name, String test_class) {
////        return null;
////    }
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossiblePredicates(String test_name, String test_class) {
////        return null;
////    }
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleScenarios(String test_name, String test_class) {
////        return null;
////    }
//
//    //Todo: NoAssertion
//
//    //    private String core_action = "";
////
////    private Set<String> core_predicate = new LinkedHashSet<>();
////
////    private String core_scenario = "";
////
////    private Project cur_project = ProjectManager.getInstance().getOpenProjects()[0];
////
////    private void reportNull(){
////        System.out.println("Project is Null!");
////    }
////
////
////    @Override
////    public List<CtMethod> fetchAllMatchedCases(String test_class) {
////
////        String current_project = cur_project.getBasePath();
////
////        Launcher launcher1 = new Launcher();
////
////        List<CtMethod> results = new LinkedList<>();
////
////        if (current_project !=  null){
////
////            launcher1.addInputResource(current_project);
////        }
////        else {
////
////            reportNull();
////        }
////
////        launcher1.buildModel();
////
////        CtModel ctModel1 = launcher1.getModel();
////
////        //Build the model for the tested program
////
////
////        for (CtPackage ctPackage : ctModel1.getAllPackages()){
////
////            for (CtType<?> ctType : ctPackage.getTypes()){
////
////                if (ctType.isPublic()
////                        && ctType.isPublic()) {
////
////                    for (CtMethod<?> method : ctType.getMethods()) {
////
////                        if (method.getAnnotation(Test.class) == null) continue;
////
////                        List<CtStatement> body = method.getBody().getStatements();
////
////                        if (body.size() != 3) continue;
////
////                        CtStatement first_line = method.getBody().getStatement(0);
////
////                        CtStatement second_line = method.getBody().getStatement(1);
////
////                        CtStatement last_line = method.getBody().getLastStatement();
////
////                        if ((first_line instanceof CtLocalVariable
////                                && !(second_line instanceof CtInvocation))
////                                ||
////                                (first_line instanceof CtInvocation
////                                && !(second_line instanceof CtLocalVariable))) continue;
////
////                        if (!(last_line instanceof CtInvocation)
////                                || !last_line.toString().contains("assert")) continue;
////
////
////                        //Preset the structure of the test pattern
////
////                        //First line is the instantiation of the scenario / action
////
////                        //Second line is the action / instantiation of the scenario
////
////                        //Last line is the assertion
////
////                        results.add(method);
////                    }
////                }
////            }
////        }
////
////        return results;
////    }
////
////    @Override
////    public String getCoreAction(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test =
////                this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtStatement first_line = method.getBody().getStatement(0);
////
////                CtStatement second_line = method.getBody().getStatement(1);
////
////
////                if (first_line instanceof CtInvocation){
////
////                    core_action = ((CtInvocation) first_line).getExecutable().getSimpleName();
////
////                }
////
////                else if (second_line instanceof CtInvocation){
////
////                    core_action = ((CtInvocation) second_line).getExecutable().getSimpleName();
////
////                }
////
////            }
////        }
////
////        return core_action;
////    }
////
////    @Override
////    public Set<String> getCorePredicate(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test =
////                this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtStatement last_line = method.getBody().getLastStatement();
////
////                List<CtInvocation> invocationList =
////                        last_line.filterChildren(new TypeFilter<>(CtInvocation.class)).list();
////
////                for (CtInvocation invocation : invocationList){
////
////                    if (invocation.getExecutable().getSimpleName().contains("assert")) continue;
////
////                    core_predicate.add(invocation.getExecutable().getSimpleName());
////                }
////
////                //Remove "assert...()"
////            }
////        }
////
////        return core_predicate;
////    }
////
////    @Override
////    public String getCoreScenario(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test =
////                this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                CtStatement first_line = method.getBody().getStatement(0);
////
////                CtStatement second_line = method.getBody().getStatement(1);
////
////
////                if (first_line instanceof CtLocalVariable){
////
////                    core_scenario = ((CtLocalVariable) first_line).getSimpleName();
////
////                }
////
////                else if (second_line instanceof CtLocalVariable){
////
////                    core_scenario = ((CtLocalVariable) second_line).getSimpleName();
////
////                }
////            }
////        }
////
////        return core_scenario;
////    }
////
////
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleActions(String test_name, String test_class) {
////        return null;
////    }
////    @Deprecated
////    @Override
////    public List<String> getAllPossiblePredicates(String test_name, String test_class) {
////        return null;
////    }
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleScenarios(String test_name, String test_class) {
////        return null;
////    }
//
//    //Todo: Normal
//
//    //    public int num_of_catches = 3;
////
////    private Project cur_project = ProjectManager.getInstance().getOpenProjects()[0];
////
////    private String core_action = "";
////
////    private Set<String> core_predicate = new LinkedHashSet<>();
////
////    private String core_scenario = "";
////
////    private void reportNull() {
////        System.out.println("Project is Null!");
////    }
////    @Override
////    public List<CtMethod> fetchAllMatchedCases(String test_class) {
////
////        String current_project = cur_project.getBasePath();
////
////        Launcher launcher1 = new Launcher();
////
////        List<CtMethod> results = new LinkedList<>();
////
////        if (current_project != null) {
////            launcher1.addInputResource(current_project);
////        } else {
////            reportNull();
////        }
////
////        launcher1.buildModel();
////
////        CtModel model1 = launcher1.getModel();
////
////        //Build the model for the tested program
////
////        for (CtPackage p : model1.getAllPackages()) {
////
////            for (CtType<?> type : p.getTypes()) {
////
////                if (type.isClass() && type.isPublic()
////                        && (type.getSimpleName().contains("Test")
////                        || type.getSimpleName().contains("test"))) {
////
////                    for (CtMethod<?> method : type.getMethods()) {
////
////                        //Extract test cases with only one try-catch statement
////
////                        if (method.getAnnotation(Test.class) == null) {
////                            continue;
////                        }
////
////                        CtStatement first_line = method.getBody().getStatement(0);
////
////                        if (!(first_line instanceof CtLocalVariable)) continue;
////                        //First Line must be a local variable as the scenario
////
////                        List<CtTry> tryStatements = method.getBody().getStatements().stream()
////                                .filter(CtTry.class::isInstance)
////                                .map(CtTry.class::cast)
////                                .collect(Collectors.toList());
////                        //Fetch All Try-Catch in the test body
////
////                        if (tryStatements.size() != 1) {
////                            continue;
////                        }
////
////                        List<CtStatement> body = tryStatements.get(0).getBody().getStatements();
////
////                        if (body.size() != 2) {
////                            continue;
////                        }
////
////                        CtStatement first = body.get(0);
////                        CtStatement second = body.get(1);
////
////                        if(!(second instanceof CtInvocation)) {
////
////                            CtInvocation<?> invocation = (CtInvocation<?>) second;
////
////                            if(!invocation.getExecutable().getSimpleName().equals("fail")) {
////                                continue;
////                            }
////                        }
////
////                        if(!(first instanceof CtInvocation)) {
////                            continue;
////                        }
////
////                        List<CtCatch> catchList = tryStatements.get(0).getCatchers();
////
////                        if (catchList.size() > num_of_catches) continue;
////
////                        boolean catch_indicator = false;
////
////                        for (CtCatch ctCatch : catchList){
////
////                            List<CtStatement> catch_body = ctCatch.getBody().getStatements();
////
////                            if (catch_body.size() > 1){
////
////                                catch_indicator = true;
////                            }
////                        }
////
////                        if (catch_indicator) continue;
////
////                        //Exclude any catch size greater than 1
////
////                        results.add(method);
////                        //Add the element
////                    }
////                }
////
////            }
////        }
////
////        return results;
////    }
////
////
////    @Override
////    public String getCoreAction(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test){
////
////            if (method.getSimpleName().equals(test_name)){
////
////                List<CtTry> tryStatements = method.getBody().getStatements().stream()
////                        .filter(CtTry.class::isInstance)
////                        .map(CtTry.class::cast)
////                        .collect(Collectors.toList());
////
////                CtTry cur_try = tryStatements.get(0);
////
////                List<CtCatch> catches = cur_try.getCatchers();
////
////                CtInvocation<?> invocation = (CtInvocation<?>)
////                        tryStatements.get(0).getBody().getStatements().get(0);
////
////                String action = invocation.getExecutable().getSimpleName();
////
////                //Actual Action for the selected test case
////
////                core_action = action;
////
////                for (CtCatch ctCatch : catches){
////
////                    for (CtStatement statement : ctCatch.getBody().getStatements()){
////
////                        if (!(statement instanceof CtInvocation)) continue;
////
////                        CtInvocation<?> invocation_catches = (CtInvocation<?>) statement;
////
////                        if(core_predicate
////                                .contains(invocation_catches
////                                        .getExecutable().getSimpleName())) continue;
////                        //Exclude all the repeating element from the list
////
////                        core_predicate.add(invocation_catches.getExecutable().getSimpleName());
////                    }
////                }
////                //Get the first verb in the first catch statement
////            }
////        }
////
////        return core_action;
////    }
////
////    @Override
////    public Set<String> getCorePredicate(String test_name, String test_class) {
////
////        return core_predicate;
////    }
////
////    @Override
////    public String getCoreScenario(String test_name, String test_class) {
////
////        List<CtMethod> all_matched_test = this.fetchAllMatchedCases(test_class);
////
////        for(CtMethod method : all_matched_test) {
////
////            if (!method.getSimpleName().equals(test_name)) continue;
////
////            CtStatement first_line = method.getBody().getStatement(0);
////
////            CtLocalVariable<?> scenario_line = (CtLocalVariable) first_line;
////
////            core_scenario = scenario_line.getSimpleName();
////
////            //Get the first statement in the test body to extract the scenario
////        }
////
////        return core_scenario;
////    }
////
////
////
////
////
////
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleActions(String test_name, String test_class){
////
////
////        return null;
////
//////        for(CtMethod method:all_matched_test){
//////
//////            if(method.getSimpleName().equals(test_name)){
//////
//////                List<CtExecutableReferenceImpl> cur_invocations=
//////                        method.filterChildren(new TypeFilter<>(CtInvocation.class)).
//////                                map((CtFunction<CtInvocation, CtExecutableReference>)
//////                                        CtAbstractInvocation::getExecutable).list();
//////
//////                CtBlock cur_block=method.getBody();
//////
//////                CtTry try_statement=null;
//////
//////                for(CtStatement statement:
//////                        cur_block.getStatements()){
//////
//////                    if(statement.getClass().equals(CtTryImpl.class)){
//////
//////                        try_statement=(CtTry)statement;
//////
//////                        //System.out.println("try catcher: " + ArrayUtils.toString(try_statement.getCatchers()));
//////
//////                        break;
//////
//////                    }
//////                }
//////                //Get the first try-catch statement's catcher part
//////
//////                for(CtExecutableReference invocations:
//////                        cur_invocations){
//////
//////                    if(try_statement!=null&&
//////                            ArrayUtils.toString(try_statement.getCatchers()).contains(invocations.toString()))continue;
//////                    //Exclude all invocations in the catcher part
//////
//////                    actions.add(invocations.getSimpleName());
//////
//////                }
//////
//////            }
//////        }
////    }
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossiblePredicates(String test_name,String test_class){
////
////        List<String> predicates=new LinkedList<>();
////
////        List<CtMethod> all_matched_test=this.fetchAllMatchedCases(test_class);
////
////
////
////
////
////
////        return predicates;
////
//////        int times = 0;
//////
//////        for(CtMethod method:all_matched_test){
//////
//////            if(method.getSimpleName().equals(test_name)){
//////                times++;
//////
//////                List<CtExecutableReferenceImpl> cur_invocations=
//////                        method.filterChildren(new TypeFilter<>(CtInvocation.class)).
//////                                map((CtFunction<CtInvocation, CtExecutableReference>)
//////                                        CtAbstractInvocation::getExecutable).list();
//////
//////                CtBlock cur_block=method.getBody();
//////
//////                CtTry try_statement=null;
//////
//////                for(CtStatement statement:
//////                        cur_block.getStatements()){
//////
//////                    if(statement.getClass().equals(CtTryImpl.class)){
//////
//////                        try_statement=(CtTry)statement;
//////
//////                        //System.out.println("try catcher: " + ArrayUtils.toString(try_statement.getCatchers()));
//////
//////                        break;
//////
//////                    }
//////                }
//////                //Get the first try-catch statement's catcher part
//////
//////                //System.out.println("Current invocs: " + ArrayUtils.toString(cur_invocations));
//////
//////                for(CtExecutableReference invocations:
//////                        cur_invocations){
//////
//////                    if(try_statement!=null&&
//////                            ArrayUtils.toString(try_statement.getCatchers())
//////                                    .contains(invocations.toString())){
//////
//////                        //System.out.println("Selected Invocs: " + invocations.getSimpleName());
//////
//////                        predicates.add(invocations.getSimpleName());
//////                    }
//////                    //Include all invocations in the catcher part
//////                }
//////
//////            }
//////        }
//////
//////        //System.out.println("times: " + times);
////    }
////
////    @Deprecated
////    @Override
////    public List<String> getAllPossibleScenarios(String test_name,String test_class){
////
////        List<String> scenarios=new LinkedList<>();
////
////        List<CtMethod> all_matched_test=this.fetchAllMatchedCases(test_class);
////
////
////
////
////
////
////        return scenarios;
////
//////        for(CtMethod method:all_matched_test){
//////
//////            if(method.getSimpleName().equals(test_name)){
//////
//////                CtBlock cur_block=method.getBody();
//////
//////                for(CtStatement statement:cur_block.getStatements()){
//////
//////                    CtLocalVariable<?> localVariable;
//////
//////
//////                    if(statement.getClass().equals(CtLocalVariableImpl.class)){
//////
//////                        localVariable=(CtLocalVariable)statement;
//////
//////
//////                        scenarios.add(localVariable.getSimpleName());
//////                    }else if(statement.getClass().equals(CtTryImpl.class)){
//////
//////                        CtTry try_statement=(CtTry)statement;
//////
//////                        List<CtElement> list_embedded_variable=statement.filterChildren
//////                                (new TypeFilter<CtElement>(CtElement.class){
//////                                    @Override
//////                                    public boolean matches(CtElement element){
//////                                        return super.matches(element)
//////                                                &&element.getClass().equals(CtLocalVariableImpl.class);
//////                                    }
//////                                }).list();
//////                        //Get all elements from a statement
//////
//////                        for(CtElement element:list_embedded_variable){
//////
//////                            CtLocalVariable ctLocalVariable_embedded=(CtLocalVariable)element;
//////
//////                            if(ArrayUtils.toString(try_statement.getCatchers())
//////                                    .contains(ctLocalVariable_embedded.getSimpleName()))continue;
//////
//////                            scenarios.add(ctLocalVariable_embedded.getSimpleName());
//////                        }
//////                        //Add embedded objects from a statement as possible scenarios
//////                    }
//////
//////                }
//////                //Get all objects
//////
//////            }
//////        }
////    }
//
//    //Todo: TryCatch
//}
