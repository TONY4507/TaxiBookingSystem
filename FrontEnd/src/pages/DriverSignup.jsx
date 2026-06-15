import React, { useState } from 'react'
import { Paper, Box, TextField, Button, Typography, Stack } from '@mui/material'
import api from '../api'
import { useNavigate } from 'react-router-dom'
import SectionHeader from '../components/SectionHeader'

export default function DriverSignup(){
  const [form, setForm] = useState({ username:'', email:'', password:'', licenseNumber:'', mobileNumber:'' })
  const [error, setError] = useState(null)
  const nav = useNavigate()

  const submit = async () => {
    try {
      await api.post('/drivers/signup', form)
      nav('/login')
    } catch (e) {
      setError(e.response?.data || e.message)
    }
  }

  return (
    <Paper sx={{ p: 4, maxWidth: 620, mx: 'auto', borderRadius: 4, boxShadow: 3 }}>
      <SectionHeader title="Driver Registration" subtitle="Join TaxiBook and start accepting rides." />
      <Stack spacing={2}>
        <TextField label="Full name" value={form.username} onChange={e => setForm({ ...form, username: e.target.value })} fullWidth />
        <TextField label="Email address" type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} fullWidth />
        <TextField label="Password" type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} fullWidth />
        <TextField label="License number" value={form.licenseNumber} onChange={e => setForm({ ...form, licenseNumber: e.target.value })} fullWidth />
        <TextField label="Phone number" value={form.mobileNumber} onChange={e => setForm({ ...form, mobileNumber: e.target.value })} fullWidth />
        {error && <Typography color="error">{String(error)}</Typography>}
        <Button variant="contained" size="large" onClick={submit} fullWidth>
          Register Driver
        </Button>
      </Stack>
    </Paper>
  )
}
