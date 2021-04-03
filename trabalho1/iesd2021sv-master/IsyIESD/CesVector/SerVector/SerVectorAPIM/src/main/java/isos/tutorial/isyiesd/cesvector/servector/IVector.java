package isos.tutorial.isyiesd.cesvector.servector;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "IVector", targetNamespace = "http://iesd21.isos.isyiesd.cesvector.servectorserver")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface IVector {

    @WebMethod
    int read(int pos);

    @WebMethod
    void write(int pos, int n);

}
