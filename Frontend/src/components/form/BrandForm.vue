<script setup>
import { ref, watchEffect } from 'vue'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const emit = defineEmits(['submitAction'])
const props = defineProps({
    brandData: Object,
    pathName: String
})

const oldBrand = ref(null)
const newBrand = ref({
    name: props.brandData?.name || '',
    websiteUrl: props.brandData?.websiteUrl || '',
    countryOfOrigin: props.brandData?.countryOfOrigin || '',
    isActive: props.brandData?.isActive ?? true
})

oldBrand.value = {...newBrand.value}

const disabled = ref(true)

function handleClick() {
    disabled.value = true
    emit('submitAction', newBrand.value)
}

watchEffect(() => {
    const hasEmptyField = newBrand.value.name === ''
    const unchanged = JSON.stringify(newBrand.value) === JSON.stringify(oldBrand.value)

    disabled.value = hasEmptyField || unchanged
})

const cancel = () => {
    router.push({ name: props.pathName })
}

</script>
 
<template>
  <div class="flex items-center justify-center">
    <div class="bg-white border border-gray-200 shadow-md rounded-lg p-10 w-130">
      <div class="grid gap-5">
        <div class="grid gap-3">
          <div class="grid gap-1.5">
            <FormInput v-model="newBrand.name" label="Brand Name" :required="true" inputType="text" :maxlength="30"
                  placeholder="Enter brand name" className="itbms-name"/>
            </div>
          </div>

          <div class="grid gap-1.5">
            <FormInput v-model="newBrand.websiteUrl" label="Website URL" inputType="url" :maxlength="40"
                placeholder="Enter Website URL" className="itbms-websiteUrl"/>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-10 gap-3">
            <div class="md:col-span-7">
              <FormInput v-model="newBrand.countryOfOrigin" label="Country of Origin" inputType="text" 
                placeholder="Enter country" className="itbms-countryOfOrigin"/>
            </div>
            <div class="md:col-span-3 flex flex-col items-center justify-center gap-3 font-medium text-lg">
              <p class="font-rubik text-[#332A1E]">Active</p>
              <input v-model="newBrand.isActive" type="checkbox" class="itbms-isActive toggle toggle-lg custom-toggle"/>
            </div>
          </div>

          <div class="flex justify-center gap-4 pt-2">
            <BaseButton @click="handleClick" text="Save" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-save-button w-full" :disabled="disabled"/>
            <BaseButton @click="cancel" v-model="newBrand.isActive" text="Cancel" class="itbms-cancel-button w-full"/>
          </div>
        </div>
    </div>
  </div>
</template>
 
<style scoped>
.custom-toggle {
  background-color: #d1d5db;
  border: 2px
}
.custom-toggle::before {
  background-color: #ffffff
}
.custom-toggle:checked {
  background-color: #4bbd80 
}
.custom-toggle:checked::before {
  background-color: #ffffff
}
</style>
