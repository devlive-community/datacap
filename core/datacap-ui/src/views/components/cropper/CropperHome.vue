<template>
  <div>
    <Button class="p-0 w-full">
      <Input ref="uploadInput" type="file" accept="image/jpg, image/jpeg, image/png, image/gif" @change="selectFile"/>
    </Button>
  </div>

  <div v-if="result.blobURL" class="pt-2 grid place-items-center">
    <div>
      <img class="max-h-[180px]" :src="result.blobURL" alt=""/>
    </div>
  </div>

  <Dialog v-if="isShowModal" :is-visible="isShowModal" :title="$t('common.cropper')" @close="isShowModal = $event">
    <div class="p-0">
      <VuePictureCropper style="max-height: 400px" :boxStyle="{ width: '100%', height: '100%', backgroundColor: '#f8f8f8', margin: 'auto' }"
                         :options="{ viewMode: 1, dragMode: 'crop', }" :img="pic" @ready="ready"/>
    </div>
    <template #footer>
      <div class="space-x-2">
        <Button class="btn" size="sm" @click="isShowModal = false">
          {{ $t('common.cancel') }}
        </Button>
        <Button class="btn" size="sm" variant="destructive" @click="clear">
          {{ $t('common.clear') }}
        </Button>
        <Button class="btn" size="sm" variant="destructive" @click="reset">
          {{ $t('common.reset') }}
        </Button>
        <Button class="btn primary" size="sm" @click="getResult">
          {{ $t('common.cropper') }}
        </Button>
      </div>
    </template>
  </Dialog>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue'
import VuePictureCropper, { cropper } from 'vue-picture-cropper'
import Button from '@/views/ui/button'
import { Input } from '@/components/ui/input'
import Dialog from '@/views/ui/dialog'

export default defineComponent({
  components: {
    Dialog,
    Input,
    VuePictureCropper,
    Button
  },
  setup(props, { emit })
  {
    console.debug(props)

    const isShowModal = ref<boolean>(false)
    const uploadInput = ref<HTMLInputElement | null>(null)
    const pic = ref<string>('')
    const result = reactive({
      blobURL: ''
    })

    function selectFile(e: Event)
    {
      pic.value = ''
      result.blobURL = ''

      const { files } = e.target as HTMLInputElement
      if (!files || !files.length) {
        return
      }

      const file = files[0]
      const reader = new FileReader()
      reader.readAsDataURL(file)
      reader.onload = () => {
        pic.value = String(reader.result)
        isShowModal.value = true
        if (!uploadInput.value) {
          return
        }
        uploadInput.value.value = ''
      }
    }

    async function getResult()
    {
      if (!cropper) {
        return
      }
      const blob: Blob | null = await cropper.getBlob()
      if (!blob) {
        return
      }
      result.blobURL = URL.createObjectURL(blob)
      isShowModal.value = false
      const file = await cropper.getFile()
      emit('update:value', file)
    }

    function clear()
    {
      if (!cropper) {
        return
      }
      cropper.clear()
    }

    function reset()
    {
      if (!cropper) {
        return
      }
      cropper.reset()
    }

    function ready()
    {
      console.log('Cropper is ready.')
    }

    return {
      uploadInput,
      pic,
      result,
      isShowModal,
      selectFile,
      getResult,
      clear,
      reset,
      ready
    }
  }
})
</script>
