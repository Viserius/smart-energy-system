import { STATE, MUTATIONS } from "../../../src/store/modules/current_household";
import {SET_CURRENT_HOUSEHOLD} from "../../../src/store/modules/current_household.methods";

it('sets the current household using a commit', () => {
  const state = STATE;
  const household = {
    id: 'DUMMY_HOUSEHOLD',
    name: 'Dummy Household',
  };

  MUTATIONS[SET_CURRENT_HOUSEHOLD](state, household);

  expect(state).toStrictEqual({
    currentHousehold: {
      id: 'DUMMY_HOUSEHOLD',
      name: 'Dummy Household',
    }
  });
});
