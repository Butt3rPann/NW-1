<script setup>
import { ref } from 'vue';
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'

const user = ref({
  email: '',
  password: ''
})

const isNull = ref({
    email: false,
    password: false
})

const handleDisabledButton = (field, value) => {
    isNull.value[field] = value
}
</script>
 
<template>
<div class="w-full font-rubik bg-white ">
    <div class="flex flex-col items-center h-190">
        <div class="bg-[#F2EDEC] shadow-md w-220 flex h-100 rounded-lg px-3 py-40">
            <div class="flex flex-col">
            <FormInput v-model="user.email" label="Email" :required="true" inputType="text" :maxlength="50" field="email"
                placeholder="Enter email"  className="itbms-email" />
            <FormInput v-model="user.password" label="Password" :required="true" inputType="text" :maxlength="14" field="password"
                placeholder="Enter password"  className="itbms-password" />
            </div>
            <div class="flex justify-seButton @click="handleClick" text="Submit" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-submit-button w-full" :disabled="disabled"/>
                <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
            </div>
        </div>
    </div>
</div>
</template>
 
<style scoped>

</style>
<script setup>
import { ref } from 'vue';
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'

const user = ref({
  email: '',
  password: ''
})

const isNull = ref({
    email: false,
    password: false
})

const handleDisabledButton = (field, value) => {
    isNull.value[field] = value
}
</script>
 
<template>
<div class="w-full font-rubik bg-white ">
    <div class="flex flex-col items-center h-190">
        <div class="bg-[#F2EDEC] shadow-md w-220 flex h-100 rounded-lg px-3 py-40">
            <div class="flex flex-col">
            <FormInput v-model="user.email" label="Email" :required="true" inputType="text" :maxlength="50" field="email"
                placeholder="Enter email"  className="itbms-email" />
            <FormInput v-model="user.password" label="Password" :required="true" inputType="text" :maxlength="14" field="password"
                placeholder="Enter password"  className="itbms-password" />
            </div>
        </div>
    </div>
</div>
</template>
 
<style scoped>

</style>