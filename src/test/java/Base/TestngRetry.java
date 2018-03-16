package Base;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class TestngRetry implements IRetryAnalyzer {
    private int retryCount         = 0;
    private int maxRetryCount     = 3;   // retry a failed test 3 additional times

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount <maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}