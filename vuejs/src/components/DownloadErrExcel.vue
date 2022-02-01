<template>
  <div>
    <v-col align="center">
      <!-- <v-alert dense text max-width="450" type="error">
        Uploaded file: <strong>{{ filename }}</strong> <br> had some error(s) !
      </v-alert> -->

      <h3>Uploaded file: <span style="color:red">{{ filename }}</span> had some error(s) !</h3>
      <v-btn class="ma-2" outlined color="red" @click="downloadFile()">
        Download Error.xlsx
        <v-icon right dark> mdi-cloud-download </v-icon>
      </v-btn>
    </v-col>
  </div>
</template>

<script>
export default {
  name: "DownloadErrExcel",
  props: {
    filename: String,
  },
  methods: {
    downloadFile() {
      console.log(this.filename);
    },
  },
};
</script>

<style>
</style>