import React from 'react';
import {RouteView} from "fengwuxp-routing-core";
import {AntdRouteViewOptions} from "fengwuxp-routing-antd";


export interface HomeViewProps {

}

interface HomeViewState {

}

@RouteView<AntdRouteViewOptions>({})
export default class HomeView extends React.Component<HomeViewProps, HomeViewState> {
  render(): React.ReactElement {
    return <div>

    </div>
  }
}
