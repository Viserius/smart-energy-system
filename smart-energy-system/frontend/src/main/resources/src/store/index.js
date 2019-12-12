import 'es6-promise/auto';
import Vue from 'vue';
import Vuex from 'vuex';
import energyUsage from './modules/energy_history';
import household from '../store/modules/current_household';

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    energyUsage,
    household,
  },
});
