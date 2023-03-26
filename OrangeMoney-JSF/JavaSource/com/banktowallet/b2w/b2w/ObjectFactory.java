
package com.banktowallet.b2w.b2w;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.banktowallet.b2w.b2w package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetMiniStatement_QNAME = new QName("http://b2w.banktowallet.com/b2w", "GetMiniStatement");
    private final static QName _GetMiniStatementResponse_QNAME = new QName("http://b2w.banktowallet.com/b2w", "GetMiniStatementResponse");
    private final static QName _WalletToAccountTransferResponse_QNAME = new QName("http://b2w.banktowallet.com/b2w", "WalletToAccountTransferResponse");
    private final static QName _CancelTransferResponse_QNAME = new QName("http://b2w.banktowallet.com/b2w", "CancelTransferResponse");
    private final static QName _GetAccountBalanceResponse_QNAME = new QName("http://b2w.banktowallet.com/b2w", "GetAccountBalanceResponse");
    private final static QName _TransferStatusInquiryResponse_QNAME = new QName("http://b2w.banktowallet.com/b2w", "TransferStatusInquiryResponse");
    private final static QName _GetAccountBalance_QNAME = new QName("http://b2w.banktowallet.com/b2w", "GetAccountBalance");
    private final static QName _WalletToAccountTransfer_QNAME = new QName("http://b2w.banktowallet.com/b2w", "WalletToAccountTransfer");
    private final static QName _CancelTransfer_QNAME = new QName("http://b2w.banktowallet.com/b2w", "CancelTransfer");
    private final static QName _TransferStatusInquiry_QNAME = new QName("http://b2w.banktowallet.com/b2w", "TransferStatusInquiry");
    private final static QName _AccountToWalletTransferResponse_QNAME = new QName("http://b2w.banktowallet.com/b2w", "AccountToWalletTransferResponse");
    private final static QName _AccountToWalletTransfer_QNAME = new QName("http://b2w.banktowallet.com/b2w", "AccountToWalletTransfer");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.banktowallet.b2w.b2w
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CancelTransfer }
     * 
     */
    public CancelTransfer createCancelTransfer() {
        return new CancelTransfer();
    }

    /**
     * Create an instance of {@link GetAccountBalance }
     * 
     */
    public GetAccountBalance createGetAccountBalance() {
        return new GetAccountBalance();
    }

    /**
     * Create an instance of {@link WalletToAccountTransfer }
     * 
     */
    public WalletToAccountTransfer createWalletToAccountTransfer() {
        return new WalletToAccountTransfer();
    }

    /**
     * Create an instance of {@link GetMiniStatementResponse }
     * 
     */
    public GetMiniStatementResponse createGetMiniStatementResponse() {
        return new GetMiniStatementResponse();
    }

    /**
     * Create an instance of {@link WalletToAccountTransferResponse }
     * 
     */
    public WalletToAccountTransferResponse createWalletToAccountTransferResponse() {
        return new WalletToAccountTransferResponse();
    }

    /**
     * Create an instance of {@link GetMiniStatement }
     * 
     */
    public GetMiniStatement createGetMiniStatement() {
        return new GetMiniStatement();
    }

    /**
     * Create an instance of {@link CancelTransferResponse }
     * 
     */
    public CancelTransferResponse createCancelTransferResponse() {
        return new CancelTransferResponse();
    }

    /**
     * Create an instance of {@link GetAccountBalanceResponse }
     * 
     */
    public GetAccountBalanceResponse createGetAccountBalanceResponse() {
        return new GetAccountBalanceResponse();
    }

    /**
     * Create an instance of {@link TransferStatusInquiryResponse }
     * 
     */
    public TransferStatusInquiryResponse createTransferStatusInquiryResponse() {
        return new TransferStatusInquiryResponse();
    }

    /**
     * Create an instance of {@link AccountToWalletTransferResponse }
     * 
     */
    public AccountToWalletTransferResponse createAccountToWalletTransferResponse() {
        return new AccountToWalletTransferResponse();
    }

    /**
     * Create an instance of {@link AccountToWalletTransfer }
     * 
     */
    public AccountToWalletTransfer createAccountToWalletTransfer() {
        return new AccountToWalletTransfer();
    }

    /**
     * Create an instance of {@link TransferStatusInquiry }
     * 
     */
    public TransferStatusInquiry createTransferStatusInquiry() {
        return new TransferStatusInquiry();
    }

    /**
     * Create an instance of {@link CancelTransfer.TranRequestInfo }
     * 
     */
    public CancelTransfer.TranRequestInfo createCancelTransferTranRequestInfo() {
        return new CancelTransfer.TranRequestInfo();
    }

    /**
     * Create an instance of {@link GetAccountBalance.AccountBalanceInquiryRequest }
     * 
     */
    public GetAccountBalance.AccountBalanceInquiryRequest createGetAccountBalanceAccountBalanceInquiryRequest() {
        return new GetAccountBalance.AccountBalanceInquiryRequest();
    }

    /**
     * Create an instance of {@link WalletToAccountTransfer.MobileTransferRequest }
     * 
     */
    public WalletToAccountTransfer.MobileTransferRequest createWalletToAccountTransferMobileTransferRequest() {
        return new WalletToAccountTransfer.MobileTransferRequest();
    }

    /**
     * Create an instance of {@link GetMiniStatementResponse.Return }
     * 
     */
    public GetMiniStatementResponse.Return createGetMiniStatementResponseReturn() {
        return new GetMiniStatementResponse.Return();
    }

    /**
     * Create an instance of {@link WalletToAccountTransferResponse.Return }
     * 
     */
    public WalletToAccountTransferResponse.Return createWalletToAccountTransferResponseReturn() {
        return new WalletToAccountTransferResponse.Return();
    }

    /**
     * Create an instance of {@link GetMiniStatement.MiniStatementRequest }
     * 
     */
    public GetMiniStatement.MiniStatementRequest createGetMiniStatementMiniStatementRequest() {
        return new GetMiniStatement.MiniStatementRequest();
    }

    /**
     * Create an instance of {@link CancelTransferResponse.Return }
     * 
     */
    public CancelTransferResponse.Return createCancelTransferResponseReturn() {
        return new CancelTransferResponse.Return();
    }

    /**
     * Create an instance of {@link GetAccountBalanceResponse.Return }
     * 
     */
    public GetAccountBalanceResponse.Return createGetAccountBalanceResponseReturn() {
        return new GetAccountBalanceResponse.Return();
    }

    /**
     * Create an instance of {@link TransferStatusInquiryResponse.Return }
     * 
     */
    public TransferStatusInquiryResponse.Return createTransferStatusInquiryResponseReturn() {
        return new TransferStatusInquiryResponse.Return();
    }

    /**
     * Create an instance of {@link AccountToWalletTransferResponse.Return }
     * 
     */
    public AccountToWalletTransferResponse.Return createAccountToWalletTransferResponseReturn() {
        return new AccountToWalletTransferResponse.Return();
    }

    /**
     * Create an instance of {@link AccountToWalletTransfer.MobileTransferRequest }
     * 
     */
    public AccountToWalletTransfer.MobileTransferRequest createAccountToWalletTransferMobileTransferRequest() {
        return new AccountToWalletTransfer.MobileTransferRequest();
    }

    /**
     * Create an instance of {@link TransferStatusInquiry.TranRequestInfo }
     * 
     */
    public TransferStatusInquiry.TranRequestInfo createTransferStatusInquiryTranRequestInfo() {
        return new TransferStatusInquiry.TranRequestInfo();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMiniStatement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "GetMiniStatement")
    public JAXBElement<GetMiniStatement> createGetMiniStatement(GetMiniStatement value) {
        return new JAXBElement<GetMiniStatement>(_GetMiniStatement_QNAME, GetMiniStatement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMiniStatementResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "GetMiniStatementResponse")
    public JAXBElement<GetMiniStatementResponse> createGetMiniStatementResponse(GetMiniStatementResponse value) {
        return new JAXBElement<GetMiniStatementResponse>(_GetMiniStatementResponse_QNAME, GetMiniStatementResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WalletToAccountTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "WalletToAccountTransferResponse")
    public JAXBElement<WalletToAccountTransferResponse> createWalletToAccountTransferResponse(WalletToAccountTransferResponse value) {
        return new JAXBElement<WalletToAccountTransferResponse>(_WalletToAccountTransferResponse_QNAME, WalletToAccountTransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "CancelTransferResponse")
    public JAXBElement<CancelTransferResponse> createCancelTransferResponse(CancelTransferResponse value) {
        return new JAXBElement<CancelTransferResponse>(_CancelTransferResponse_QNAME, CancelTransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountBalanceResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "GetAccountBalanceResponse")
    public JAXBElement<GetAccountBalanceResponse> createGetAccountBalanceResponse(GetAccountBalanceResponse value) {
        return new JAXBElement<GetAccountBalanceResponse>(_GetAccountBalanceResponse_QNAME, GetAccountBalanceResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferStatusInquiryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "TransferStatusInquiryResponse")
    public JAXBElement<TransferStatusInquiryResponse> createTransferStatusInquiryResponse(TransferStatusInquiryResponse value) {
        return new JAXBElement<TransferStatusInquiryResponse>(_TransferStatusInquiryResponse_QNAME, TransferStatusInquiryResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAccountBalance }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "GetAccountBalance")
    public JAXBElement<GetAccountBalance> createGetAccountBalance(GetAccountBalance value) {
        return new JAXBElement<GetAccountBalance>(_GetAccountBalance_QNAME, GetAccountBalance.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link WalletToAccountTransfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "WalletToAccountTransfer")
    public JAXBElement<WalletToAccountTransfer> createWalletToAccountTransfer(WalletToAccountTransfer value) {
        return new JAXBElement<WalletToAccountTransfer>(_WalletToAccountTransfer_QNAME, WalletToAccountTransfer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CancelTransfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "CancelTransfer")
    public JAXBElement<CancelTransfer> createCancelTransfer(CancelTransfer value) {
        return new JAXBElement<CancelTransfer>(_CancelTransfer_QNAME, CancelTransfer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TransferStatusInquiry }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "TransferStatusInquiry")
    public JAXBElement<TransferStatusInquiry> createTransferStatusInquiry(TransferStatusInquiry value) {
        return new JAXBElement<TransferStatusInquiry>(_TransferStatusInquiry_QNAME, TransferStatusInquiry.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountToWalletTransferResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "AccountToWalletTransferResponse")
    public JAXBElement<AccountToWalletTransferResponse> createAccountToWalletTransferResponse(AccountToWalletTransferResponse value) {
        return new JAXBElement<AccountToWalletTransferResponse>(_AccountToWalletTransferResponse_QNAME, AccountToWalletTransferResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AccountToWalletTransfer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://b2w.banktowallet.com/b2w", name = "AccountToWalletTransfer")
    public JAXBElement<AccountToWalletTransfer> createAccountToWalletTransfer(AccountToWalletTransfer value) {
        return new JAXBElement<AccountToWalletTransfer>(_AccountToWalletTransfer_QNAME, AccountToWalletTransfer.class, null, value);
    }

}
