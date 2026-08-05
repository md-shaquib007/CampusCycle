param(
    [Parameter(Mandatory = $true)]
    [string]$BaseUrl
)

$base = $BaseUrl.TrimEnd('/')
$checks = @(
    @{ Name = 'health'; Url = "$base/health"; Expected = 200 },
    @{ Name = 'home'; Url = "$base/home"; Expected = 200 },
    @{ Name = 'listings'; Url = "$base/listings"; Expected = 200 },
    @{ Name = 'login'; Url = "$base/login"; Expected = 200 },
    @{ Name = 'protected redirect'; Url = "$base/profile"; Expected = 302 }
)

$failed = 0
foreach ($check in $checks) {
    try {
        $response = Invoke-WebRequest -Uri $check.Url -Method Get -MaximumRedirection 0 -ErrorAction Stop
        $status = [int]$response.StatusCode
    } catch {
        if ($_.Exception.Response -ne $null) {
            $status = [int]$_.Exception.Response.StatusCode
        } else {
            $status = -1
        }
    }

    if ($status -eq $check.Expected) {
        Write-Host "PASS  $($check.Name) ($status)" -ForegroundColor Green
    } else {
        Write-Host "FAIL  $($check.Name): expected $($check.Expected), received $status" -ForegroundColor Red
        $failed++
    }
}

if ($failed -gt 0) {
    exit 1
}

Write-Host 'Smoke test passed.' -ForegroundColor Green
