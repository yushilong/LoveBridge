
package com.lovebridge.chat.utils;

import android.os.AsyncTask;

public abstract class CancelableAsyncTask {

    private final AsyncTask asyncTask = new AsyncTask() {


        protected Object doInBackground(Object aobj[]) {
            return CancelableAsyncTask.this.doInBackground(aobj);
        }

        protected void onPostExecute(Object obj) {
            if (!cancelled) {
                CancelableAsyncTask.this.onPostExecute(obj);
            }
        }

        protected void onPreExecute() {
            CancelableAsyncTask.this.onPreExecute();
        }

    };
    private volatile boolean cancelled;

    public CancelableAsyncTask() {
        cancelled = false;
    }

    public boolean cancel(boolean flag) {
        cancelled = true;
        return asyncTask.cancel(flag);
    }

    protected abstract Object doInBackground(Object aobj[]);

    public CancelableAsyncTask execute(Object aobj[]) {
        asyncTask.execute(aobj);
        return this;
    }

    public CancelableAsyncTask executeInParallel(Object aobj[]) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, aobj);
            return this;
        } else {
            asyncTask.execute(aobj);
            return this;
        }
    }

    protected boolean isCancelled() {
        return cancelled;
    }

    protected abstract void onPostExecute(Object obj);

    protected void onPreExecute() {
    }

}
