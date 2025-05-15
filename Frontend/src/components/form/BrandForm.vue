<script setup>
import { ref , watchEffect} from 'vue'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue';
import { useRouter } from 'vue-router';

const router = useRouter()

const emit = defineEmits(['submitAction'])

const props = defineProps({
    brandData: Object,
    path: String
})

const oldBrand = ref(null)

const newBrand = ref({
    name: props.brandData?.name || '',
    websiteUrl: props.brandData?.websiteUrl || '',
    countryOfOrigin: props.brandData?.countryOfOrigin || ''
})

oldBrand.value = {...newBrand.value}

const disabled = ref(false)

function handleClick() {
    Object.keys(newBrand.value).forEach(key => {
        if (newBrand.value[key] === '') {
            newBrand.value[key] = null
        }
    })

    emit('submitAction', newBrand.value)
}

const cancel = () => {
    router.push({ path: props.path })
}

</script>
 
<template>
  <div class="flex items-center justify-center">
    <div class="bg-white border border-gray-200 shadow-md rounded-lg p-10 w-130">
      <div class="grid gap-5">
        <div class="grid gap-3">
          <div class="grid gap-1.5">
            <FormInput v-model="newBrand.name" label="Brand Name" :required="true" inputType="text"
                  placeholder="Enter brand name" className="itbms-brand-name"/>
            </div>
          </div>

          <div class="grid gap-1.5">
            <FormInput v-model="newBrand.websiteUrl" label="Website URL" :required="true" inputType="url"
                placeholder="Enter Website URL" className="itbms-brand-url"/>
          </div>

          <div class="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div class="grid gap-1.5">
              <FormInput v-model="newBrand.countryOfOrigin" label="Country of Origin" :required="true" inputType="text" 
                  placeholder="Enter country" className="itbms-brand-country"/>
            </div>
          </div>

          <div class="flex justify-center gap-4 pt-2">
            <BaseButton @click="handleClick" text="Save" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-save-button w-full" :disabled="disabled"/>
            <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
          </div>
        </div>
    </div>
  </div>
</template>
 
<style scoped></style>