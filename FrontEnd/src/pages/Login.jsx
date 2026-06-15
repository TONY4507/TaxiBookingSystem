import React, { useState } from 'react'
import { Paper, Box, TextField, Button, Typography, ToggleButton, ToggleButtonGroup, Stack, CircularProgress } from '@mui/material'
import api from '../api'
import { useNavigate } from 'react-router-dom'
import SectionHeader from '../components/SectionHeader'

export default function Login(){
  const [role, setRole] = useState('customer')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState(null)
  const [loading, setLoading] = useState(false)
  const nav = useNavigate()

  const handleLogin = async () => {
    setError(null)
    setLoading(true)
    try {
      const url = role === 'customer' ? '/customers/login' : '/drivers/login'
      const res = await api.post(url, null, { params: { email, password } })
      const dto = res.data || {}
      const userObj = {
        ...dto,
        role,
        id: dto.CustomerId || dto.Driverid || dto.DriverId || dto.customerId || dto.driverId || dto.id,
        name: dto.username || dto.name || dto.username,
      }
      localStorage.setItem('user', JSON.stringify(userObj))
      nav(role === 'driver' ? '/driver' : '/customer')
    } catch (e) {
      setError(e.response?.data || e.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <Paper sx={{ p: 4, maxWidth: 520, mx: 'auto', borderRadius: 4, boxShadow: 3 }}>
      <SectionHeader title="Sign in to TaxiBook" subtitle="Enter your credentials and choose your role to continue." />
      <Stack spacing={2}>
        <ToggleButtonGroup value={role} exclusive onChange={(e, val) => val && setRole(val)} sx={{ alignSelf: 'center' }}>
          <ToggleButton value="customer">Customer</ToggleButton>
          <ToggleButton value="driver">Driver</ToggleButton>
        </ToggleButtonGroup>
        <TextField label="Email address" type="email" value={email} onChange={e => setEmail(e.target.value)} fullWidth />
        <TextField label="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} fullWidth />
        {error && <Typography color="error">{String(error)}</Typography>}
        <Button size="large" variant="contained" onClick={handleLogin} fullWidth disabled={loading}>
          {loading ? <CircularProgress size={24} color="inherit" /> : 'Continue'}
        </Button>
      </Stack>
    </Paper>
  )
}
