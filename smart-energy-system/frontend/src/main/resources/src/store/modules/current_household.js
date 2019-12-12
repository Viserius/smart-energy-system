import {
  GET_HOUSEHOLD,
  SET_CURRENT_HOUSEHOLD,
  CHANGE_HOUSEHOLD,
  RESET_CURRENT_HOUSEHOLD,
} from './current_household.methods';
import {
  SUBSCRIBE,
  UNSUBSCRIBE,
} from './energy_history.methods';

export const STATE = {
  currentHousehold: {},
};

export const MUTATIONS = {
  [SET_CURRENT_HOUSEHOLD]: (state, household) => {
    state.currentHousehold = household;
  },
};

export const GETTERS = {
  [GET_HOUSEHOLD]: state => state.currentHousehold,
};

export const ACTIONS = {
  [CHANGE_HOUSEHOLD]: (context, household) => {
    context.commit(SET_CURRENT_HOUSEHOLD, household);
    context.dispatch(SUBSCRIBE, household.id);
  },

  [RESET_CURRENT_HOUSEHOLD]: (context) => {
    context.dispatch(UNSUBSCRIBE);
    context.commit(SET_CURRENT_HOUSEHOLD, {});
  },
};

export default {
  state: STATE,
  mutations: MUTATIONS,
  getters: GETTERS,
  actions: ACTIONS,
};
