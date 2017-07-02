// IPlayerService.aidl
package reed.flyingreed;

// Declare any non-default types here with import statements
import reed.flyingreed.model.Model;

interface IPlayerService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */

    void next();
    void prev();
    void pause();
    void start();
    void stop();
    long getCurrentPosition();
    long getDuration();
    void initWithFavor(int favor);
    boolean isPlaying();
    int getFavor();
}
