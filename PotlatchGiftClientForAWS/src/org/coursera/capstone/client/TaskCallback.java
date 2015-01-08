
package org.coursera.capstone.client;

public interface TaskCallback<T> {

    public void success(T result);

    public void error(Exception e);

}
