package com.btow.om;

public class IdlePortProxy implements com.btow.om.IdlePort {
  private String _endpoint = null;
  private com.btow.om.IdlePort idlePort = null;
  
  public IdlePortProxy() {
    _initIdlePortProxy();
  }
  
  public IdlePortProxy(String endpoint) {
    _endpoint = endpoint;
    _initIdlePortProxy();
  }
  
  private void _initIdlePortProxy() {
    try {
      idlePort = (new com.btow.om.IdleServiceLocator()).getIdlePort();
      if (idlePort != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)idlePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)idlePort)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (idlePort != null)
      ((javax.xml.rpc.Stub)idlePort)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.btow.om.IdlePort getIdlePort() {
    if (idlePort == null)
      _initIdlePortProxy();
    return idlePort;
  }
  
  public boolean setIdle(java.lang.String idle) throws java.rmi.RemoteException{
    if (idlePort == null)
      _initIdlePortProxy();
    return idlePort.setIdle(idle);
  }
  
  
}