import React, { useState } from 'react'
import { Paper, Box, TextField, Button, Typography, Stack } from '@mui/material'
import api from '../api'
import { useNavigate } from 'react-router-dom'
import SectionHeader from '../components/SectionHeader'

export default function CustomerSignup(){
  const [form, setForm] = useState({ username:'', email:'', password:'', mobileNumber:'' })
  const [error, setError] = useState(null)
  const nav = useNavigate()

  const submit = async () => {
    try {
      await api.post('/customers/signup', form)
      nav('/login')
    } catch (e) {
      setError(e.response?.data || e.message)
    }
  }

  return (
    <Paper sx={{ p: 4, maxWidth: 620, mx: 'auto', borderRadius: 4, boxShadow: 3 }}>
      <SectionHeader title="Customer Registration" subtitle="Create a new account to book rides quickly." />
      <Stack spacing={2}>
        <TextField label="Full name" value={form.username} onChange={e => setForm({ ...form, username: e.target.value })} fullWidth />
        <TextField label="Email address" type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} fullWidth />
        <TextField label="Password" type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} fullWidth />
        <TextField label="Phone number" value={form.mobileNumber} onChange={e => setForm({ ...form, mobileNumber: e.target.value })} fullWidth />
        {error && <Typography color="error">{String(error)}</Typography>}
        <Button variant="contained" size="large" onClick={submit} fullWidth>
          Create Account
        </Button>
      </Stack>
    </Paper>
  )
}
