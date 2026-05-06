import React, { useState } from 'react';
import { Button } from './components/ui/Button';
import { FrameworkSelector } from './components/ui/FrameworkSelector';
import { Input } from './components/ui/Input';
import { ArchitectureBuilder } from './components/ui/ArchitectureBuilder';
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
    features: ['MODELS', 'CONTROLLERS', 'SERVICES', 'REPOSITORIES'],
    models: []
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

  const handleModelsChange = (newModels) => {
    setFormData(prev => ({ ...prev, models: newModels }));
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

    // Transform models array to the required JSON format
    const transformedModels = {};
    for (const m of formData.models) {
      if (!m.name) continue;
      const fieldsObj = {};
      for (const f of m.fields) {
        if (!f.name) continue;
        fieldsObj[f.name] = f.type;
      }
      transformedModels[m.name] = fieldsObj;
    }

    const payload = {
      ...formData,
      models: transformedModels
    };

    try {
      const response = await fetch('http://localhost:8080/api/v1/projects/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(payload),
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
    <div className="min-h-screen bg-zinc-950 flex flex-col pt-8 pb-12 px-4 sm:px-6 lg:px-8 font-sans">
      <div className="max-w-7xl mx-auto w-full">
        <div className="mb-8 flex items-center space-x-3">
          <div className="inline-flex items-center justify-center p-3 bg-red-600 text-white shadow-sm">
            <Zap className="w-6 h-6" />
          </div>
          <div>
            <h1 className="text-3xl font-black tracking-tight text-zinc-50">
              GoPlan Engine
            </h1>
            <p className="text-sm text-zinc-400 font-medium">
              Configure architecture, models, and scaffold instantly.
            </p>
          </div>
        </div>

        <form onSubmit={handleSubmit}>
          <div className="flex flex-col lg:flex-row gap-8">
            {/* Left Column: Settings */}
            <div className="w-full lg:w-1/3 space-y-6">
              <div className="bg-zinc-900 border-2 border-zinc-800 p-6">
                <h2 className="text-xl font-bold text-zinc-50 mb-4 pb-2 border-b-2 border-zinc-800">
                  Project Details
                </h2>
                <div className="space-y-4">
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

              <div className="bg-zinc-900 border-2 border-zinc-800 p-6">
                <h2 className="text-xl font-bold text-zinc-50 mb-4 pb-2 border-b-2 border-zinc-800">
                  Framework Selection
                </h2>
                <FrameworkSelector
                  options={FRAMEWORK_OPTIONS}
                  value={formData.framework}
                  onChange={handleFrameworkChange}
                />
              </div>

              {error && (
                <div className="p-4 bg-red-950/30 border-2 border-red-900 flex items-start">
                  <AlertCircle className="w-5 h-5 text-red-500 mt-0.5 mr-3 flex-shrink-0" />
                  <p className="text-sm font-medium text-red-400">{error}</p>
                </div>
              )}

              <div className="pt-2 sticky top-6">
                <Button 
                  type="submit" 
                  loading={loading} 
                  className="w-full text-base py-3 bg-red-600 hover:bg-red-700 text-white uppercase tracking-wider font-black shadow-sm"
                >
                  <Download className="w-5 h-5 mr-2" />
                  Generate Project
                </Button>
              </div>
            </div>

            {/* Right Column: Architecture Builder */}
            <div className="w-full lg:w-2/3">
              <div className="bg-zinc-900 border-2 border-zinc-800 p-6 min-h-full">
                <ArchitectureBuilder 
                  models={formData.models} 
                  onModelsChange={handleModelsChange} 
                  featureOptions={FEATURE_OPTIONS}
                  features={formData.features}
                  onFeaturesChange={handleFeaturesChange}
                />
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
}

export default App;
