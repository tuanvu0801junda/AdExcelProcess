<template>
  <div>
    <v-spacer></v-spacer>
    <v-col>
      <h2 class="upload-file-header">Welcome to Advertising Excel Processor</h2>
    </v-col>

    <v-spacer></v-spacer>
    <v-form>
      <v-file-input
        counter
        max-width="600"
        truncate-length="100"
        placeholder="Select an .xlsx file"
        accept=".xlsx"
      >
      </v-file-input>

      <v-spacer></v-spacer>
      <v-col align="center">
        <v-btn class="ma-2" outlined color="blue" @click="submit">
          Submit
          <v-icon>mdi-format-list-bulleted-square</v-icon>
        </v-btn>
      </v-col>
    </v-form>
  </div>
</template>

<script>
export default {
    name: 'UploadFile',
    // data(){
    //   return {
        
    //   }
    // },
    methods:{
      submit(){
        console.log("Hello")
      }
    }
};
</script>

<style>
.upload-file-header {
  text-align: center;
  font-family: "Times New Roman", Times, serif;
}
</style>