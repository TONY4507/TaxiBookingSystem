import React, { useState } from 'react'
import { Paper, Box, TextField, Button, Typography, ToggleButton, ToggleButtonGroup, Stack, CircularProgress } from '@mui/material'
import api, { setAuthToken } from '../api'
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
      const url = role === 'customer' ? '/customers/login' : role === 'driver' ? '/drivers/login' : '/admin/login'
      const res = await api.post(url, null, { params: { email, password } })
      const data = res.data || {}
      const userObj = {
        role: data.role?.replace('ROLE_', '').toLowerCase(),
        token: data.token,
        id: data.userId,
        username: data.username,
        email: data.email,
        approvalStatus: data.approvalStatus,
      }
      setAuthToken(data.token)
      localStorage.setItem('user', JSON.stringify(userObj))
      if (role === 'admin') nav('/admin')
      else if (role === 'driver') nav('/driver')
      else nav('/customer')
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
          <ToggleButton value="admin">Admin</ToggleButton>
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
