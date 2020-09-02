package org.goskyer.sshcore;

public class ServerInfo {

    private String nickname;

    private UserInfo userInfo;

    private CPUInfo cpuInfo;

    private MemoryInfo memoryInfo;

    private DiskInfo diskInfo;

    private NetworkInfo networkInfo;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public CPUInfo getCpuInfo() {
        return cpuInfo;
    }

    public void setCpuInfo(CPUInfo cpuInfo) {
        this.cpuInfo = cpuInfo;
    }

    public MemoryInfo getMemoryInfo() {
        return memoryInfo;
    }

    public void setMemoryInfo(MemoryInfo memoryInfo) {
        this.memoryInfo = memoryInfo;
    }

    public DiskInfo getDiskInfo() {
        return diskInfo;
    }

    public void setDiskInfo(DiskInfo diskInfo) {
        this.diskInfo = diskInfo;
    }

    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }

    public void setNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }

}
