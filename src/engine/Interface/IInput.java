package engine.Interface;

import engine.Input.KeyBoard;
import engine.Input.Mouse;

public interface IInput<T> {
    void input(KeyBoard key, Mouse mouse, T obj) ;
}
