package org.nachain.core.dapp.internal.fullnode.events;


public class ChangeDTO {

    private long id;


    private String beneficiaryAddress;

    public long getId() {
        return id;
    }

    public ChangeDTO setId(long id) {
        this.id = id;
        return this;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public ChangeDTO setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
        return this;
    }

    public ChangeDTO() {
    }

    public ChangeDTO(long id, String beneficiaryAddress) {
        this.id = id;
        this.beneficiaryAddress = beneficiaryAddress;
    }

    @Override
    public String toString() {
        return "ChangeDTO{" +
                "id=" + id +
                ", beneficiaryAddress='" + beneficiaryAddress + '\'' +
                '}';
    }
}
