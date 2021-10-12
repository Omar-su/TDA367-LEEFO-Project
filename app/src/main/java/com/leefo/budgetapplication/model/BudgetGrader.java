package com.leefo.budgetapplication.model;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class BudgetGrader {

    private final TransactionModel transactionModel;

    public BudgetGrader(TransactionModel transactionModel) {
        this.transactionModel = transactionModel;
    }

    public ArrayList<Category> getAllBudgetCategories() {
        ArrayList<Category> budgetList = new ArrayList<>();
        for (Category c : transactionModel.getCategoryList()) {
            if (c.getGoal() > 0) {
                budgetList.add(c);
            }
        }
        return budgetList;
    }

    public ArrayList<Category> getBudgetCategoriesByMonth(TransactionRequest request) {
        return transactionModel.removeEmptyCategories(getAllBudgetCategories(),
                request);
    }

    public float gradeCategory(TransactionRequest request) {
        float budgetingOutcome = getRoundedBudgetOutcome(request);
        return getGrade(budgetingOutcome);
    }

    public float getRoundedBudgetOutcome(TransactionRequest request) {
        float categoryBudget = request.getCategory().getGoal();
        float categoryActualExpenseSum = transactionModel.getTransactionSum(request);

        float nonRoundedBudgetingOutcome = (categoryActualExpenseSum / categoryBudget);

        return (float) (Math.round(nonRoundedBudgetingOutcome * 100.0) / 100.0);

    }

    public float getAverageGrades(TransactionRequest request) {
        List<Category> budgetCategories = getBudgetCategoriesByMonth(request);
        float gradeTotal = (float) 0.0;
        for (Category c : budgetCategories) {
            gradeTotal += gradeCategory(new TransactionRequest(c, request.getMonth(), request.getYear()));
        }
        return gradeTotal / budgetCategories.size();
    }

    private float getGrade(double budgetingOutcome) {
        // Lower boundaries for grades

        // Grade 5: 0-0.65
        double grade5Lower = 0;

        // Grade 4.5: 0.65-0.8
        double grade4point5Lower = 0.65;

        // Grade 4: 0.8-0.9
        double grade4Lower = 0.8;

        // Grade 3.5: 0.9-0.95
        double grade3point5Lower = 0.9;

        // Grade 3: 0.95-1.05
        double grade3Lower = 0.95;

        // Grade 2.5: 1.05-1.15
        double grade2point5Lower = 1.05;

        // Grade 2: 1.15-1.25.
        double grade2Lower = 1.15;

        // Grade 1.5: 1.25-1.45
        double grade1point5Lower = 1.25;

        // Grade 1: 1.45-1.60
        double grade1Lower = 1.45;

        // Grade 0.5: 1.60-2.0
        double grade0point5Lower = 1.60;

        // Grade 0: 2.0>
        double grade0Lower = 2.0;

        //Boundaries are lower inclusive => grade 4 includes grade4Lower to but not including
        // grade3point5Lower
        TreeMap<Double, Double> gradeMap = new TreeMap<>();
        gradeMap.put(grade5Lower, 5.0);
        gradeMap.put(grade4point5Lower, 4.5);
        gradeMap.put(grade4Lower, 4.0);
        gradeMap.put(grade3point5Lower, 3.5);
        gradeMap.put(grade3Lower, 3.0);
        gradeMap.put(grade2point5Lower, 2.5);
        gradeMap.put(grade2Lower, 2.0);
        gradeMap.put(grade1point5Lower, 1.5);
        gradeMap.put(grade1Lower, 1.0);
        gradeMap.put(grade0point5Lower, 0.5);
        gradeMap.put(grade0Lower, 0.0);

        double actualGrade = gradeMap.floorEntry(budgetingOutcome).getValue();
        return (float) actualGrade;

    }


}
