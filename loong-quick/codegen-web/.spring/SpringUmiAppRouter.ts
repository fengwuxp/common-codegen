import {
  RouterCommandMethod,
  AbstractAppCommandRouter,
  RouteMapping,
} from 'fengwuxp-declarative-router-adapter';
import { SpringAppRouterInterface } from './SpringAppRouterInterface';

import { HomeViewProps0 } from './SpringAppRouterInterface';

export default class SpringUmiAppRouter
  extends AbstractAppCommandRouter
  implements SpringAppRouterInterface {
  @RouteMapping('/home/index')
  homeIndex: RouterCommandMethod<Partial<HomeViewProps0>>;
}
