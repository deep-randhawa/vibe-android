package cs490.team_15.vibe.API;

import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by drandhaw on 11/19/16.
 */
public abstract class VibeCallback<T> implements Callback<T> {
    private Throwable throwable;

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        throwable = new VibeException(t.getMessage(), t);
    }

    public boolean didErrorOccur() {
        return throwable != null;
    }

    public Throwable getErrorThrowable() {
        return this.throwable;
    }
}

class VibeException extends Exception {
    public VibeException() {
    }

    public VibeException(String detailMessage) {
        super(detailMessage);
    }

    public VibeException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public VibeException(Throwable throwable) {
        super(throwable);
    }
}