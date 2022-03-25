import managers.ExpenseManager;
import managers.FileWorkerCsv;

public class SplitExpenseManager {
    private final static String PATH_INPUT_FILE = "src/Files/input.csv";
    private final static String PATH_OUTPUT_FILE = "src/Files/output.csv";
    public static void main(String[] args) {
        FileWorkerCsv fileWorkerCsv = new FileWorkerCsv(PATH_INPUT_FILE,PATH_OUTPUT_FILE);
        ExpenseManager expenseManager = new ExpenseManager(fileWorkerCsv);
        expenseManager.loadFile();
        expenseManager.writeFile();

    }

}
