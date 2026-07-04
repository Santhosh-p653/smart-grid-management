import React, { useState } from 'react';

const TelemetryChart = ({ data, type = 'bar', xKey, yKey, title }) => {
  const [hoveredIndex, setHoveredIndex] = useState(null);
  const [tooltipPos, setTooltipPos] = useState({ x: 0, y: 0 });

  if (!data || data.length === 0) {
    return (
      <div style={{ display: 'flex', height: '200px', alignItems: 'center', justifyContent: 'center', color: 'var(--text-muted)' }}>
        No analytics data available
      </div>
    );
  }

  const values = data.map(item => item[yKey]);
  const maxValue = Math.max(...values, 10);
  
  // Dimensions
  const width = 500;
  const height = 200;
  const paddingLeft = 40;
  const paddingRight = 20;
  const paddingTop = 20;
  const paddingBottom = 30;
  
  const chartWidth = width - paddingLeft - paddingRight;
  const chartHeight = height - paddingTop - paddingBottom;
  
  const barWidth = chartWidth / data.length - 10;

  const handleMouseMove = (e, index) => {
    const rect = e.currentTarget.getBoundingClientRect();
    const x = e.clientX - rect.left + 15;
    const y = e.clientY - rect.top - 40;
    setHoveredIndex(index);
    setTooltipPos({ x, y });
  };

  const handleMouseLeave = () => {
    setHoveredIndex(null);
  };

  return (
    <div style={{ position: 'relative' }}>
      <h4 style={{ fontSize: '1rem', fontWeight: 600, marginBottom: '1rem', color: 'var(--text-secondary)' }}>{title}</h4>
      
      <svg viewBox={`0 0 ${width} ${height}`} className="svg-chart">
        <defs>
          <linearGradient id="barGrad" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stopColor="var(--accent-cyan)" />
            <stop offset="100%" stopColor="var(--accent-blue)" stopOpacity="0.2" />
          </linearGradient>
          <linearGradient id="lineGrad" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0%" stopColor="var(--accent-purple)" stopOpacity="0.4" />
            <stop offset="100%" stopColor="var(--accent-purple)" stopOpacity="0" />
          </linearGradient>
        </defs>

        {/* Gridlines */}
        {[0, 0.25, 0.5, 0.75, 1].map((ratio, i) => {
          const y = paddingTop + chartHeight * (1 - ratio);
          return (
            <g key={i}>
              <line 
                x1={paddingLeft} 
                y1={y} 
                x2={width - paddingRight} 
                y2={y} 
                stroke="var(--glass-border)" 
                strokeDasharray="4 4" 
              />
              <text 
                x={paddingLeft - 8} 
                y={y + 4} 
                fill="var(--text-muted)" 
                fontSize="8" 
                textAnchor="end"
              >
                {Math.round(maxValue * ratio)}
              </text>
            </g>
          );
        })}

        {/* Columns / Bars */}
        {type === 'bar' && data.map((item, index) => {
          const val = item[yKey];
          const barHeight = (val / maxValue) * chartHeight;
          const x = paddingLeft + index * (chartWidth / data.length) + 5;
          const y = height - paddingBottom - barHeight;

          return (
            <rect
              key={index}
              x={x}
              y={y}
              width={barWidth}
              height={Math.max(barHeight, 2)}
              rx="4"
              className="svg-bar"
              onMouseMove={(e) => handleMouseMove(e, index)}
              onMouseLeave={handleMouseLeave}
            />
          );
        })}

        {/* Area Line Chart alternative */}
        {type === 'line' && (() => {
          const points = data.map((item, index) => {
            const val = item[yKey];
            const x = paddingLeft + index * (chartWidth / (data.length - 1)) + (data.length === 1 ? chartWidth/2 : 0);
            const y = height - paddingBottom - (val / maxValue) * chartHeight;
            return { x, y };
          });

          const pathD = points.reduce((acc, p, i) => 
            i === 0 ? `M ${p.x} ${p.y}` : `${acc} L ${p.x} ${p.y}`, ''
          );

          const areaD = `${pathD} L ${points[points.length - 1].x} ${height - paddingBottom} L ${points[0].x} ${height - paddingBottom} Z`;

          return (
            <g>
              <path d={areaD} fill="url(#lineGrad)" />
              <path d={pathD} fill="none" stroke="var(--accent-purple)" strokeWidth="2.5" />
              {points.map((p, index) => (
                <circle 
                  key={index}
                  cx={p.x} 
                  cy={p.y} 
                  r={hoveredIndex === index ? 6 : 4} 
                  fill={hoveredIndex === index ? 'var(--accent-cyan)' : 'var(--accent-purple)'} 
                  stroke="var(--bg-primary)"
                  strokeWidth="1.5"
                  onMouseMove={(e) => handleMouseMove(e, index)}
                  onMouseLeave={handleMouseLeave}
                  style={{ transition: 'r 0.15s ease', cursor: 'pointer' }}
                />
              ))}
            </g>
          );
        })()}

        {/* X Axis Labels */}
        {data.map((item, index) => {
          const label = item[xKey];
          const x = type === 'bar'
            ? paddingLeft + index * (chartWidth / data.length) + (barWidth / 2) + 5
            : paddingLeft + index * (chartWidth / (data.length - 1));

          return (
            <text
              key={index}
              x={x}
              y={height - 10}
              fill="var(--text-secondary)"
              fontSize="9"
              textAnchor="middle"
            >
              {label}
            </text>
          );
        })}
      </svg>

      {/* Tooltip */}
      {hoveredIndex !== null && (
        <div 
          className="glass-panel"
          style={{
            position: 'absolute',
            left: `${tooltipPos.x}px`,
            top: `${tooltipPos.y}px`,
            padding: '0.4rem 0.8rem',
            borderRadius: '6px',
            fontSize: '0.75rem',
            background: 'var(--bg-secondary)',
            borderColor: 'var(--accent-cyan)',
            zIndex: 10,
            pointerEvents: 'none',
            display: 'flex',
            flexDirection: 'column',
            gap: '0.1rem'
          }}
        >
          <span style={{ color: 'var(--text-muted)' }}>{data[hoveredIndex][xKey]}</span>
          <span style={{ fontWeight: 700, color: 'white' }}>
            {data[hoveredIndex][yKey].toLocaleString()} {yKey.includes('Consumption') || yKey.includes('consumption') ? 'kWh' : 'MW'}
          </span>
        </div>
      )}
    </div>
  );
};

export default TelemetryChart;
