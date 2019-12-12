import {createLocalVue, shallowMount} from "@vue/test-utils";
import Vuex from 'vuex';
import BootstrapVue from "bootstrap-vue";
import App from "../../../src/App";
import {GET_HOUSEHOLD} from "../../../src/store/modules/current_household.methods";


const localVue = createLocalVue();
localVue.use(Vuex);
localVue.use(BootstrapVue);

describe('App.vue', () => {
  let getters;
  let store;

  beforeEach(() => {
    getters = {
      [GET_HOUSEHOLD]: () => ({
        id: 'DUMMY_HOUSEHOLD',
        name: 'Dummy Household',
      }),
    };
    store = new Vuex.Store({
      getters,
    });
  });

  it('should render correct contents', () => {
    const wrapper = shallowMount(App, {
      stubs: {
        RouterView: true,
        BNavbarBrand: '<div class="stub-brand"><slot></slot></div>',
      },
      store,
      localVue,
    });
    const brand = wrapper.find('.stub-brand');
    expect(brand.text()).toBe('Smart Energy System');
  });
});
