package org.nachain.core.dapp.internal.bridge;

import com.fasterxml.jackson.core.type.TypeReference;
import org.nachain.core.chain.structure.instance.CoreInstanceEnum;
import org.nachain.core.chain.structure.instance.InstanceType;
import org.nachain.core.chain.transaction.context.TxContext;
import org.nachain.core.chain.transaction.context.TxEventType;
import org.nachain.core.chain.transaction.context.TxMarkService;
import org.nachain.core.util.JsonUtils;
import org.rocksdb.RocksDBException;

import java.util.concurrent.ExecutionException;

public class BindingService {

    private static BindingDAO bindingDAO;

    static {
        bindingDAO = new BindingDAO();
    }


    public static Binding toBinding(String json) {
        return JsonUtils.jsonToPojo(json, Binding.class);
    }


    public static Binding newBinding(String address, ChainName chainName, String mappingAddress) {
        Binding binding = new Binding();

        binding.setAddress(address);

        binding.setChainName(chainName);

        binding.setMappingAddress(mappingAddress);

        return binding;
    }


    public static Binding addBinding(Binding binding) throws RocksDBException {

        bindingDAO.add(binding);
        return binding;
    }


    public static TxContext<Binding> toBindingContext(String json) {
        return JsonUtils.jsonToPojo(json, new TypeReference<TxContext<Binding>>() {
        });
    }


    public static TxContext<Binding> newBindingContext(Binding binding) {

        TxContext txContext = new TxContext<Binding>().setInstanceType(InstanceType.DApp);

        txContext.setEventType(TxEventType.BRIDGE_BINDING);

        txContext.setReferrerInstance(CoreInstanceEnum.Bridge.id).setReferrerTx("");

        txContext.setCrossToInstance(CoreInstanceEnum.Bridge.id);

        txContext.setData(binding);

        txContext.setTxMark(TxMarkService.newTxMark());

        return txContext;
    }


    public static Binding get(String address, ChainName chainName) throws ExecutionException, RocksDBException {
        Binding binding = bindingDAO.get(address, chainName);
        return binding;
    }

}
