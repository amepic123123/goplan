import React, { useState } from 'react';
import { Button } from './components/ui/Button';
import { FrameworkSelector } from './components/ui/FrameworkSelector';
import { Input } from './components/ui/Input';
import { MultiSelectDropdown } from './components/ui/MultiSelectDropdown';
import { Zap, Download, AlertCircle } from 'lucide-react';

const FRAMEWORK_OPTIONS = [
  {
    id: 'SPRING_BOOT',
    title: 'Spring Boot',
    description: 'Java based production-ready application'
  },
  {
    id: 'ASP_NET',
    title: 'ASP.NET Core',
    description: 'Enterprise web framework for C#'
  },
  {
    id: 'FASTAPI',
    title: 'FastAPI',
    description: 'High performance web framework for Python'
  },
  {
    id: 'DJANGO',
    title: 'Django',
    description: 'High-level Python web framework'
  }
];

const FEATURE_OPTIONS = [
  { id: 'MODELS', label: 'Models' },
  { id: 'CONTROLLERS', label: 'Controllers' },
  { id: 'SERVICES', label: 'Services' },
  { id: 'REPOSITORIES', label: 'Repositories' }
];

function App() {
  const [formData, setFormData] = useState({
    projectName: '',
    basePackage: '',
    framework: 'SPRING_BOOT',
    features: ['MODELS', 'CONTROLLERS', 'SERVICES', 'REPOSITORIES']
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (field) => (e) => {
    setFormData(prev => ({ ...prev, [field]: e.target.value }));
  };

  const handleFrameworkChange = (id) => {
    setFormData(prev => ({ ...prev, framework: id }));
  };

  const handleFeaturesChange = (newFeatures) => {
    setFormData(prev => ({ ...prev, features: newFeatures }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    // Project Name Validations
    if (!formData.projectName || formData.projectName.trim() === '') {
      setError('Project name cannot be blank');
      setLoading(false);
      return;
    }
    if (formData.projectName.length > 255) {
      setError('Project name size must be between 0 and 255');
      setLoading(false);
      return;
    }
    const projectNameRegex = /^[a-z0-9-]+$/;
    if (!projectNameRegex.test(formData.projectName)) {
      setError('Project name can only contain lowercase letters, numbers, and hyphens');
      setLoading(false);
      return;
    }

    // Base Package Validations
    if (!formData.basePackage || formData.basePackage.trim() === '') {
      setError('Base package is required');
      setLoading(false);
      return;
    }
    const basePackageRegex = /^[a-z][a-z0-9_]*(\.[a-z0-9_]+)+[0-9a-z_]$/;
    if (!basePackageRegex.test(formData.basePackage)) {
      setError('Must be a valid Java package name (e.g., com.goplan.api)');
      setLoading(false);
      return;
    }

    // Features Validation
    if (!formData.features || formData.features.length === 0) {
      setError('Features list cannot be null');
      setLoading(false);
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/api/v1/projects/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        throw new Error(`Server responded with ${response.status}: ${response.statusText}`);
      }

      // Read response as blob
      const blob = await response.blob();
      
      // Strictly enforce the filename based on the user's project name
      const filename = `${formData.projectName ? formData.projectName.trim() : 'project'}.zip`;

      const downloadUrl = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.style.display = 'none';
      link.href = downloadUrl;
      link.setAttribute('download', filename);
      document.body.appendChild(link);
      
      // Trigger the download
      link.click();
      
      // Crucial: Wait before removing the link and revoking the URL.
      // If removed immediately, the browser ignores the 'download' attribute 
      // and falls back to the blob's generated UUID!
      setTimeout(() => {
        if (document.body.contains(link)) {
          document.body.removeChild(link);
        }
        window.URL.revokeObjectURL(downloadUrl);
      }, 2000);
    } catch (err) {
      setError(err.message || 'An error occurred while generating the project.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-zinc-950 flex flex-col items-center py-6 px-4 sm:px-6 lg:px-8">
      <div className="w-full max-w-2xl">
        <div className="mb-5 text-center">
          <div className="inline-flex items-center justify-center p-2 bg-zinc-900 border border-zinc-800 rounded-2xl mb-2">
            <Zap className="w-5 h-5 text-zinc-50" />
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-zinc-50 mb-1">
            Configure Your Engine
          </h1>
          <p className="text-sm text-zinc-400">
            Set up your software architecture and instantly download the scaffolded structure.
          </p>
        </div>

        <div className="bg-zinc-900/50 border border-zinc-800 rounded-xl p-5 sm:p-6 backdrop-blur-sm">
          <form onSubmit={handleSubmit} className="space-y-5">
            <div className="space-y-3">
              <h2 className="text-lg font-semibold text-zinc-50 pb-1 border-b border-zinc-800/80">
                Project Details
              </h2>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <Input
                  id="projectName"
                  label="Project Name"
                  placeholder="e.g., my-awesome-api"
                  value={formData.projectName}
                  onChange={handleChange('projectName')}
                  required
                />
                <Input
                  id="basePackage"
                  label="Base Package"
                  placeholder="e.g., com.example.api"
                  value={formData.basePackage}
                  onChange={handleChange('basePackage')}
                  required
                />
              </div>
            </div>

            <div className="space-y-3">
              <h2 className="text-lg font-semibold text-zinc-50 pb-1 border-b border-zinc-800/80">
                Framework Selection
              </h2>
              <FrameworkSelector
                options={FRAMEWORK_OPTIONS}
                value={formData.framework}
                onChange={handleFrameworkChange}
                featureOptions={FEATURE_OPTIONS}
                features={formData.features}
                onFeaturesChange={handleFeaturesChange}
              />
            </div>

            {error && (
              <div className="p-4 rounded-md bg-red-950/50 border border-red-900 flex items-start">
                <AlertCircle className="w-5 h-5 text-red-500 mt-0.5 mr-3 flex-shrink-0" />
                <p className="text-sm text-red-200">{error}</p>
              </div>
            )}

            <div className="pt-4 flex justify-end">
              <Button 
                type="submit" 
                loading={loading} 
                className="w-full sm:w-auto"
              >
                <Download className="w-4 h-4 mr-2" />
                Generate Project
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default App;
