<template>
  <div>
    <b-card title="All Connected Households" class="border-0 bg-transparent">
      <b-card no-body>
        <b-table
          striped hover
          :items="households"
          @row-clicked="goTo"
        ></b-table>
      </b-card>
    </b-card>
  </div>
</template>

<script>
import Axios from 'axios';
import { mapActions } from 'vuex';
import { API_HOUSEHOLDS } from '../endpoints';
import { CHANGE_HOUSEHOLD } from '../store/modules/current_household.methods';

export default {
  name: 'HouseholdOverview',
  data() {
    return {
      fields: ['id', 'name', 'isTrading'],
      households: [],
    };
  },
  methods: {
    ...mapActions([
      CHANGE_HOUSEHOLD,
    ]),
    goTo(record) {
      this[CHANGE_HOUSEHOLD](record);
      this.$router.push({ name: 'HouseholdDashboard' });
    },
    loadHouseholds() {
      Axios.get(API_HOUSEHOLDS)
        .then((response) => {
          this.households = response.data;
        })
        .catch((error) => {
          console.log(error);
          this.$bus.$emit('backendOffline');
        });
    },
  },
  mounted() {
    this.loadHouseholds();
  },
};
</script>

<style scoped>

</style>
