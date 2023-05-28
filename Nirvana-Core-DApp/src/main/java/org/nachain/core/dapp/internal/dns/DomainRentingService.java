package org.nachain.core.dapp.internal.dns;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.dapp.internal.dns.domain.DomainRenting;
import org.nachain.core.dapp.internal.dns.domain.DomainRentingDAO;
import org.nachain.core.dapp.internal.dns.events.DomainRentingDTO;
import org.nachain.core.persistence.page.PageService;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DomainRentingService {

    private static DomainRentingDAO domainRentingDAO;

    static {
        domainRentingDAO = new DomainRentingDAO();
    }


    public static DomainRenting newDomainRenting(String address, String subdomain, String domain, String ipAddress, String rentingTx, BigInteger rentingNac) {
        DomainRenting domainRent = new DomainRenting();

        domainRent.setAddress(address);

        domainRent.setSubdomain(subdomain);

        domainRent.setDomain(domain);

        domainRent.setIpAddress(ipAddress);

        domainRent.setRentingTx(rentingTx);

        domainRent.setRentingNac(rentingNac);

        return domainRent;
    }


    public static DomainRenting get(String fullDomain) throws RocksDBException {
        return domainRentingDAO.get(fullDomain);
    }


    public static boolean edit(DomainRenting domainRenting) throws RocksDBException {
        return domainRentingDAO.put(domainRenting);
    }


    public static List<DomainRenting> findAllDesc() throws ExecutionException, RocksDBException {
        List<DomainRenting> domainRents = Lists.newArrayList();

        List<String> descList = PageService.findAllDesc(domainRentingDAO.getGroupId(), DomainRenting.class);
        for (String itemHash : descList) {
            domainRents.add(domainRentingDAO.get(itemHash));
        }

        return domainRents;
    }


    public static boolean add(DomainRenting domainRenting) throws RocksDBException, ExecutionException {
        boolean rtv = domainRentingDAO.add(domainRenting);


        PageService.addItem(domainRentingDAO.getGroupId(), DomainRenting.class, domainRenting.getAddress(), domainRenting.toFullDomain());

        PageService.addItem(domainRentingDAO.getGroupId(), DomainRenting.class, domainRenting.getDomain());

        return rtv;
    }


    public static TxContext toDomainRentingDTOContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<DomainRentingDTO>>() {
        });
    }


    public static TxContext newDomainRentingContext(DomainRentingDTO domainRentingDTO) {

        TxContext txContext = new TxContext<DomainRentingDTO>().setInstanceType(InstanceType.Core);

        txContext.setEventType(TxEventType.DNS_DOMAIN_RENTING);

        txContext.setReferrerInstance(CoreInstanceEnum.DNS.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.DNS.id);

        txContext.setData(domainRentingDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


}
