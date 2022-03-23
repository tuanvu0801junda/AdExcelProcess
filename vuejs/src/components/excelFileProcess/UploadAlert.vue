<template>
  <div>
    <v-col align="center">
      <div v-if="fileName != null ? true : false">
        <h3>Uploaded file: <span style="color:red">{{ fileName }}</span></h3>
      </div>
      <v-alert max-width="450" type="error">{{ errMessage }}</v-alert>
    </v-col>
  </div>
</template>

<script>
export default {
  name: "UploadAlert",
  props: {
    errMessage: String,
    fileName: String,
  },
};
</script>

<style>
</style>