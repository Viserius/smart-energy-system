<template>
  <b-card title="Trading Overview" class="border-0 bg-transparent">
    <b-card>
      <label>Set the price for selling power in USD ¢ (per kWh)</label>
      <input v-model="household.energyTradingSellPrice"
             name="energyTradingSellPrice"
             v-on:focusout="saveFields"
             type="number"
             required/>
      <p v-if="this.household.trading">Trading is currently <strong>enabled</strong>!</p>
      <p v-if="!this.household.trading">Trading is currently <strong>disabled</strong>!</p>
      <b-button
        v-on:click="toggleTrading"
        variant="primary"
        :disabled="!this.buttonEnabled">Toggle Trading</b-button>
    </b-card>
  </b-card>
</template>

<script>
import Axios from 'axios';
import { mapGetters, mapMutations } from 'vuex';
import { API_HOUSEHOLDS } from '../../endpoints';
import { GET_HOUSEHOLD, SET_CURRENT_HOUSEHOLD } from '../../store/modules/current_household.methods';

export default {
  name: 'HouseholdTrading',
  data() {
    return {
      buttonEnabled: true,
    };
  },
  methods: {
    ...mapMutations([
      SET_CURRENT_HOUSEHOLD,
    ]),
    toggleTrading() {
      this.buttonEnabled = false;
      this.household.trading = !this.household.trading;
      // Axios update
      Axios.put(`${API_HOUSEHOLDS}/${this.household.id}`, this.household)
        .then((response) => {
          this.household = response.data;
          this.buttonEnabled = true;
        }).catch((error) => {
          console.log(error);
          this.$bus.$emit('backendOffline');
        });
    },
    saveFields() {
      // Axios update
      Axios.put(`${API_HOUSEHOLDS}/${this.household.id}`, this.household)
        .catch((error) => {
          console.log(error);
          this.$bus.$emit('backendOffline');
        });
    },
  },
  computed: {
    ...mapGetters({
      household: GET_HOUSEHOLD,
    }),
  },
  mounted() {
    Axios.get(`${API_HOUSEHOLDS}/${this.household.id}`)
      .then((response) => {
        this.household = response.data;
      }).catch((error) => {
        console.log(error);
        this.$bus.$emit('backendOffline');
      });
  },
};
</script>

<style scoped>

</style>
