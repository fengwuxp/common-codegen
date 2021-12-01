import {
    AppCommandStorage,
    GetStorageCommandMethod,
    RemoveStorageCommandMethod,
    SetStorageCommandMethod,
    GetStorageCommandMethodSync,
    SetStorageCommandMethodSync
} from 'fengwuxp-declarative-storage-adapter'
import {browserAppCommandStorageFactory} from 'fengwuxp-browser-storage'

export interface AppStorage extends AppCommandStorage {
    // 设置语言环境
    setLocaleNameSync: SetStorageCommandMethodSync<string>;
    getLocaleNameSync: GetStorageCommandMethodSync<string>;
    removeLocaleName: RemoveStorageCommandMethod;

    // 保存认证信息
    setAuthentication: SetStorageCommandMethod<any>
    getAuthentication: GetStorageCommandMethod<any>
    removeAuthentication: RemoveStorageCommandMethod;

}

export const AppStorage = browserAppCommandStorageFactory<AppStorage>();

