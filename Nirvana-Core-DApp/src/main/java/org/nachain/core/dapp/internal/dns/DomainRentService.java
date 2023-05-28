package org.nachain.core.dapp.internal.dns;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.dapp.internal.dns.domain.DomainRent;
import org.nachain.core.dapp.internal.dns.domain.DomainRentDAO;
import org.nachain.core.dapp.internal.dns.domain.DomainRenting;
import org.nachain.core.dapp.internal.dns.events.DomainRentDTO;
import org.nachain.core.persistence.page.PageService;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DomainRentService {

    private static DomainRentDAO domainRentDAO;

    static {
        domainRentDAO = new DomainRentDAO();
    }


    public static DomainRent newDomainRent(String address, String domain, BigInteger rentNac, String rentTx) {
        DomainRent domainRent = new DomainRent();

        domainRent.setAddress(address);

        domainRent.setDomain(domain);

        domainRent.setRentNac(rentNac);

        domainRent.setRentTx(rentTx);

        return domainRent;
    }


    public static DomainRent get(String domain) throws RocksDBException {
        return domainRentDAO.get(domain);
    }


    public static boolean edit(DomainRent domainRent) throws RocksDBException {
        return domainRentDAO.put(domainRent);
    }


    public static List<DomainRent> findAllDesc() throws ExecutionException, RocksDBException {
        List<DomainRent> domainRents = Lists.newArrayList();

        List<String> descList = PageService.findAllDesc(domainRentDAO.getGroupId(), DomainRent.class);
        for (String itemHash : descList) {
            domainRents.add(domainRentDAO.get(itemHash));
        }

        return domainRents;
    }


    public static boolean add(DomainRent domainRent) throws RocksDBException, ExecutionException {
        boolean rtv = domainRentDAO.add(domainRent);


        PageService.addItem(domainRentDAO.getGroupId(), DomainRenting.class, domainRent.getAddress(), domainRent.getDomain());

        PageService.addItem(domainRentDAO.getGroupId(), DomainRenting.class, domainRent.getDomain());

        return rtv;
    }


    public static TxContext toDomainRentDTOContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<DomainRentDTO>>() {
        });
    }


    public static TxContext newDomainRentContext(DomainRentDTO domainRentDTO) {

        TxContext txContext = new TxContext<DomainRentDTO>().setInstanceType(InstanceType.Core);

        txContext.setEventType(TxEventType.DNS_DOMAIN_RENT);

        txContext.setReferrerInstance(CoreInstanceEnum.DNS.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.DNS.id);

        txContext.setData(domainRentDTO);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


}
