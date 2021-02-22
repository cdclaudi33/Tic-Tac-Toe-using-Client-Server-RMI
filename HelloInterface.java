package pace;

import java.rmi.*;
public interface HelloInterface extends Remote {
  //Do not change anything in this interface.
  public char[] board(char[] a) throws RemoteException;
}
