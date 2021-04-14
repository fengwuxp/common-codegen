import {
  AppCommandRouter,
  RouterCommandMethod,
} from 'fengwuxp-declarative-router-adapter';

import { HomeViewProps as HomeViewProps0 } from '../src/pages/home/index';

export interface SpringAppRouterInterface extends AppCommandRouter {
  homeIndex: RouterCommandMethod<Partial<HomeViewProps0>>;
}

export { HomeViewProps0 } from '../src/pages/home/index';
