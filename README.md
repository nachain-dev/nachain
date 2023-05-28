<p align="center">
  <a href="https://www.nachain.org/">
      <img
      src="https://github.com/nachain-dev/nac-logos/raw/main/na-brand-2x.png"
       alt="nachain-logo">
  </a>
</p>

<h3 align="center">Network Application Chain</h3>

<p align="center">
    Born for decentralized application development.
  <br>
  <br>
  <a href="https://www.nachain.org/"><strong>Website</strong></a>
  ·
  <a href="https://scan.nachain.org/">NaScan</a>
  ·
  <a href="https://www.nachain.org/app">NaStation</a>
  ·
  <a href="https://twitter.com/nirvana_chain">Twitter</a>
  ·
  <a href="https://linktr.ee/NaChainGlobal">Linktr.ee</a>
</p>

## Overview
NA Chain is the pioneer of heterogeneous composite chain model, which integrates PowF, DPoS and DAG hybrid algorithms to rapidly improve efficiency and achieve millions of TPS in a second. The native cross-chain function is one of its advantages, which can greatly meet the network requirements of Web3.0 transmission while maintaining low cost and handling fee. Original N++ programming language to understand a certain Web development experience programmers within 5 minutes to learn, learn their own interactive system development, with N++ and NVM virtual machine to communicate, and then execute the issuance of tokens, transfer, data bus, data access, micro services and other tasks. Launch PC and desktop wallet integrated applications, such as the NaStation desktop Wallet released by NA Chain earlier, integrating functions such as merge wallet, developer store, DApp Store, miner management, smart contract, decentralized server, and decentralized file management. Interact with other blockchains through cross-chain docking.

## Project modules

### NA Chain :: Open Source Modules

|  Module | Description                                                                                                                                                             |
|---|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Nirvana-Core-Crypto` | Standard BIP44 protocol keystore, using ED25519 signature algorithm, Curve: ed25519curve, H: SHA-512                                                                    |
| `Nirvana-Core-Utils` | General toolkit, including: byte, network, system, file, json and other functions                                                                                       |
| `Nirvana-Core-Persistence` | data storage persistence layer, using the rocksdb protocol for data storage, while providing an excellent caching strategy                                              |
| `Nirvana-Core-Base` | Basic library, including common dependency objects of each module Activity, Amount Util, Chain Base                                                                     |
| `Nirvana-Core-Examiner` | NExamine, N is the abbreviation of NA, and Examine is the inspector. We also abbreviate `NE`.                                                                         |
| `Nirvana-Core-Wallet` | Provides the creation and maintenance of keys, which can generate keys through language mnemonics and save them as keystore files                                       |
| `Nirvana-Core-Config` | Configure common modules, which can realize profiles configuration, miner configuration, time zone settings, etc.                                                       |
| `Nirvana-Core-SignVerify` | Data signing and verification signature module                                                                                                                          |
| `Nirvana-Core-Networks` | Middleware that can realize P2P network communication, server, client, websocket and other communication services                                                       |
| `Nirvana-Core-Mailbox` | Broadcast data receiving and pre-cleaning processing module                                                                                                             |
| `Nirvana-Core-MailboxDAL` | Data operation layer for broadcast data processing                                                                                                                      |
| `Nirvana-Core-Miner` | Miner maintenance module                                                                                                                                                |
| `Nirvana-Core-Miner` | The data operation layer of the miner maintenance module                                                                                                                |
| `Nirvana-Core-Structure` | Related functions of instance chain, logic chain and data chain                                                                                                         |
| `Nirvana-Core-StructureDAL` | Data operation layer of instance chain, logic chain and data chain                                                                                                      |
| `Nirvana-Core-Chain` | Block data maintenance                                                                                                                                                  |
| `Nirvana-Core-ChainDAL` | Data operation layer for block data maintenance                                                                                                                         |
| `Nirvana-Core-Oracle` | Oracle machine, which can read external data, and store the external data after confirming it through super nodes                                                       |
| `Nirvana-Core-OracleDAL` | Oracle data operation layer                                                                                                                                             |
| `Nirvana-Core-DApp` | Decentralized Apps                                                                                                                                                      |
| `Nirvana-Core-DAppDAL` | Data manipulation layer for decentralized applications                                                                                                                  |
| `Nirvana-Core-Intermediate` | Intermediate layer data of each functional module                                                                                                                       |
| `Nirvana-Core-IntermediateDAL` | Data operation layer for intermediate data                                                                                                                              |
| `Nirvana-Core-Token` | Create tokens that support standard protocols and NFT protocol tokens                                                                                                   |
| `Nirvana-Core-TokenDAL` | Token data operation layer                                                                                                                                              |
| `Nirvana-Core-Transaction` | Blockchain transaction data module                                                                                                                                      |
| `Nirvana-Core-TransactionDAL` | Data operation layer for transaction data                                                                                                                               |
| `Nirvana-Core-Das` | DAS consensus algorithm, used for fast data writing, gas fee only needs 0.01U of NAC                                                                                    |
| `Nirvana-Core-DasDAL` | Data operation layer of DAS consensus algorithm                                                                                                                         |
| `Nirvana-Core-InstanceProfile` | Instance wallet data maintenance module                                                                                                                                 |
| `Nirvana-Core-InstanceProfileDAL` | The data operation layer of the instance wallet data maintenance module                                                                                                 |
| `Nirvana-Core-Npp` | Public chain applications, including smart contracts, DWeb, DApp and other application types                                                                            |
| `Nirvana-Core-NppDAL` | Data operation layer of public chain applications                                                                                                                       |
| `Nirvana-Core-Subscribe` | Data subscription function, which can only subscribe to the data of the specified instance, can realize data push subscription, and obtain the latest data in real time |
| `Nirvana-Core-Version` | Version management module                                                                                                                                               |
| `Nirvana-Core-FTP` | FTP server and protocol, which can realize FTP file transfer service                                                                                                    |
| `Nirvana-Core-UtilsLibrary` | Common toolkit                                                                                                                                                          |
| `Nirvana-Core-Natives` | Native package, through local compilation, to achieve higher processing performance                                                                                     |

### NA Chain :: Closed Source Modules

|  Module | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
|---|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `Nirvana-Miner` | The Miner module is the core program on the hardware side of the public chain mining server, <br/>mainly responsible for block packaging and data processing.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                  |
| `Nirvana-Station` | The Station module provides corresponding API and middleware for the client software, <br/>which facilitates the rapid development of terminal software for each platform. <br/>Such as Windows, Linux, Mac, Android, NA OS and other terminal client software.                                                                                                                                                                                                                                                                                                                                                                                |
| `Nirvana-DFS` | Decentralized file system provides a decentralized file storage service for the public chain, <br/>so as to store user data and DWeb HTML files                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| `Nirvana-DNS` | Domain Name System is the core super application, <br/>DNS is the abbreviation of Domain Name System (DomainNameSystem), <br/>which is used to name computers and network services organized into domain hierarchies. <br/>A domain name is composed of a string of words or abbreviations separated by dots. <br/>Each domain name corresponds to a unique IP address. <br/>There is a one-to-one correspondence between domain names and IP addresses on the Internet. <br/>This functional module also integrates decentralized domain name hosting and leasing services, <br/>so that users can obtain completely anonymous and decentralized domain name services. |
| `Nirvana-VM` | Virtual Machine virtualizes server resources and provides powerful container running capabilities for DApp and DWeb. <br/>At the same time, the integration of NPP programming language allows users to quickly realize program development and interaction                                                                                                                                                                                                                                                                                                                                                                                    |
| `Nirvana-DataFlow` | Data Flow implements business capabilities such as traffic distribution, request response, business transfer, and load balancing.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| `Nirvana-Core-Nodes` | Core Nodes include all consensus models, PoWF, DPoS, DAS, etc.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| `Nirvana-Obsolete` | The Obsolete module contains obsolete functions, protocols, etc. The functions of this module will be phased out and removed in the future.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |


## Build
Build source by maven command:
```
mvn install
```

## Developers

The docs contain helpful information about contributing code to na-chain and using na-chain APIs to build applications.

#### Quick Links

- [running nastation wallet node](https://github.com/nachain-dev/nastation-sdk-java/blob/main/doc/linux-script.txt)
- [nastation-sdk](https://github.com/nachain-dev/nastation-sdk-java)


## License
The na-chain project is licensed under the [MIT license](LICENSE).