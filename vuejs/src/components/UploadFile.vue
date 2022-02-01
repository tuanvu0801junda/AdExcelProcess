<template>
  <div>
    <v-spacer></v-spacer>
    <v-col>
      <h2 class="upload-file-header">Welcome to Advertising Excel Processor</h2>
    </v-col>

    <v-spacer></v-spacer>
    <v-form>
      <v-file-input
        @change="uploadFile()"
        id="file_sl"
        clearable
        v-model="clearSignal"
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
import axios from "axios";

export default {
  name: "UploadFile",
  data() {
    return {
      selected_file: null,
      clearSignal: null,
      emitMessage: "",
      result: 0,
    };
  },
  methods: {
    uploadFile() {
      //this.selected_file = event.target.files[0] || event.dataTransfer.files[0];
      this.selected_file = document.getElementById("file_sl").files[0];
    },
    checkName() {
      if (this.selected_file["name"].length > 250) {
        this.emitMessage = "File name has more than 250 characters !";
        return -1;
      }
      return 1;
    },
    checkSize() {
      if (this.selected_file["size"] > 1000000) {
        this.emitMessage = "File size must be less than 10MB !"; 
        return -1;
      }
      return 1;
    },
    checkNotSubmit(){
      if (this.selected_file == null) return -1;
      else return 1;
    },
    async submit() {
      this.clearSignal = null;
      this.result = this.checkNotSubmit();
      if (this.result === -1){
        this.$emit('no-file-err');
        return;
      }

      this.result = this.checkName();
      if (this.result === -1) {
        this.$emit('name-err', {str: this.emitMessage, fileName: this.selected_file["name"]});        
        return;
      }

      this.result = this.checkSize();
      if (this.result === -1) {
        this.$emit("size-err", {str: this.emitMessage, fileName: this.selected_file["name"]});
        return;
      }

      try {
        var formData = new FormData(); // body, raw, formData in Postman
        formData.append('excelFile',this.selected_file);
        const res = await axios.post("http://127.0.0.1:8080/file/upload",formData);

        if (res.data.status === 200) {
          // process successful
          this.$emit("ok-reply", {fileName: this.selected_file["name"]});
        } 
        
        if (res.data.status === 500) {
          // error occured
          this.$emit("err-reply", {fileName: this.selected_file["name"]});
        }
      } catch (err) {
        console.log(err);
      }
    },
  },
};
</script>

<style>
.upload-file-header {
  text-align: center;
  font-family: "Times New Roman", Times, serif;
}
</style>