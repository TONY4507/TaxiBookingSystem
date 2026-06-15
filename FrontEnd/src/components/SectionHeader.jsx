import { Box, Typography } from '@mui/material'

export default function SectionHeader({ title, subtitle }) {
  return (
    <Box sx={{ mb: 3, display: 'flex', flexDirection: 'column', gap: 1 }}>
      <Typography variant="h4" fontWeight={700} letterSpacing={-0.5}>
        {title}
      </Typography>
      {subtitle && (
        <Typography color="text.secondary" variant="body1">
          {subtitle}
        </Typography>
      )}
    </Box>
  )
}
