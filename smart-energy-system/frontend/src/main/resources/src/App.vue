<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="primary">
      <b-navbar-brand to="/">Smart Energy System</b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item to="/" exact-active-class="active"
                      v-if="Object.entries(this.household).length === 0">
            Households Overview
          </b-nav-item>

          <b-nav-item to="/dashboard" exact-active-class="active"
                      v-if="Object.entries(this.household).length > 0">
            Household Dashboard
          </b-nav-item>

          <b-nav-item-dropdown
            id="connected-devices-dropdown"
            text="Connected Devices"
            toggle-class="nav-link-custom"
            right
            v-if="Object.entries(this.household).length > 0"
          >
            <b-dropdown-item
              to="/connected-devices"
              exact-active-class="active"
            >All Devices</b-dropdown-item>
            <b-dropdown-item
              to="/connected-devices/appliances"
              exact-active-class="active"
            >Appliances</b-dropdown-item>
            <b-dropdown-item
              to="/connected-devices/generators"
              exact-active-class="active"
            >Generators</b-dropdown-item>
            <b-dropdown-item
              to="/connected-devices/batteries"
              exact-active-class="active"
            >Batteries</b-dropdown-item>
          </b-nav-item-dropdown>

          <b-nav-item to="/trading" exact-active-class="active"
                      v-if="Object.entries(this.household).length > 0">
            Trading</b-nav-item>
        </b-navbar-nav>

        <b-navbar-nav class="ml-auto"
                      v-if="Object.entries(this.household).length > 0">
          <b-nav-item-dropdown right>
            <template v-slot:button-content>
              User
            </template>
            <b-dropdown-item href="#">Profile</b-dropdown-item>
            <b-dropdown-item v-on:click="logOut">Sign out</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
        <data-gathering/>
        <notification />
      </b-collapse>
    </b-navbar>
    <b-container>
      <router-view></router-view>
    </b-container>
  </div>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import { GET_HOUSEHOLD, RESET_CURRENT_HOUSEHOLD } from './store/modules/current_household.methods';
import DataGathering from './components/DataGathering';
import Notification from './components/Notification';

export default {
  name: 'App',
  components: {
    DataGathering,
    Notification,
  },
  computed: {
    ...mapGetters({
      household: GET_HOUSEHOLD,
    }),
  },
  methods: {
    ...mapActions([
      RESET_CURRENT_HOUSEHOLD,
    ]),
    logOut() {
      this[RESET_CURRENT_HOUSEHOLD]();
      this.$router.push({ name: 'HouseholdOverview' });
    },
  },
};
</script>

<style>

</style>
