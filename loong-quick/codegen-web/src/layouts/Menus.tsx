import {MenuDataItem} from "@umijs/route-utils";
import {SmileOutlined} from "@ant-design/icons";
import React from "react";

const menus: MenuDataItem[] = [
    {
        name: "首页",
        path: "/home",
        icon: <SmileOutlined/>
    },
    {
        name: "代码仓库",
        path: "/code_repositories",
        icon: <SmileOutlined/>
    }
]


export default menus
