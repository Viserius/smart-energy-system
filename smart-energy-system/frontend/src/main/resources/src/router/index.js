import Vue from 'vue';
import Router from 'vue-router';
import Store from '../store';
import HouseholdDashboard from '../components/HouseholdDashboard';
import ConnectedDevices from '../components/ConnectedDevices';
import Appliances from '../components/ConnectedDevices/Appliances';
import Appliance from '../components/ConnectedDevices/Appliance';
import Generators from '../components/ConnectedDevices/Generators';
import Generator from '../components/ConnectedDevices/Generator';
import Batteries from '../components/ConnectedDevices/Batteries';
import Battery from '../components/ConnectedDevices/Battery';
import Trading from '../components/HouseholdDashboard/HouseholdTrading';
import HouseholdOverview from '../components/HouseholdOverview';

Vue.use(Router);

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'HouseholdOverview',
      component: HouseholdOverview,
    },
    {
      path: '/dashboard',
      name: 'HouseholdDashboard',
      component: HouseholdDashboard,
    },
    {
      path: '/connected-devices',
      name: 'ConnectedDevices',
      component: ConnectedDevices,
    },
    {
      path: '/connected-devices/appliances',
      name: 'Appliances',
      component: Appliances,
    },
    {
      path: '/connected-devices/appliances/:id',
      name: 'Appliance',
      component: Appliance,
      props: true,
    },
    {
      path: '/connected-devices/generators',
      name: 'Generators',
      component: Generators,
    },
    {
      path: '/connected-devices/generators/:id',
      name: 'Generator',
      component: Generator,
      props: true,
    },
    {
      path: '/connected-devices/batteries',
      name: 'Batteries',
      component: Batteries,
    },
    {
      path: '/connected-devices/batteries/:id',
      name: 'Battery',
      component: Battery,
      props: true,
    },
    {
      path: '/trading',
      name: 'Trading',
      component: Trading,
    },
  ],
});

const householdLoginGuard = (to, from, next) => {
  if (to.name !== 'HouseholdOverview') {
    if (Object.entries(Store.getters.getHousehold).length === 0) {
      next(from);
    } else {
      next();
    }
  } else {
    next();
  }
};
router.beforeEach(householdLoginGuard);

export default router;
