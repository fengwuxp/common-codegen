import {AppStorage} from "@/AppStorage";


export const storageAuthentication = (authorization) => {
    AppStorage.setAuthentication(authorization).then(() => {
        console.log("success");
    }).catch((error) => {
        console.log("error", error);
    })
}

export const getAuthentication = () => {
    return AppStorage.getAuthentication()
}
