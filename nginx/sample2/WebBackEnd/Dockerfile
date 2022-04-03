#See https://aka.ms/containerfastmode to understand how Visual Studio uses this Dockerfile to build your images for faster debugging.

FROM mcr.microsoft.com/dotnet/aspnet:6.0 AS base
WORKDIR /app
#EXPOSE 80
#EXPOSE 443
EXPOSE 5000
EXPOSE 5001
EXPOSE 5002

ENV ASPNETCORE_URLS=http://+:5000;http://+:5001;http://+:5002

RUN adduser -u 5678 --disabled-password --gecos "" appuser && chown -R appuser /app
USER appuser

FROM mcr.microsoft.com/dotnet/sdk:6.0 AS build
WORKDIR /src
COPY ["WebBackEnd.csproj", "."]
RUN dotnet restore "./WebBackEnd.csproj"
COPY . .
WORKDIR "/src/."
RUN dotnet build "WebBackEnd.csproj" -c Release -o /app/build

FROM build AS publish
RUN dotnet publish "WebBackEnd.csproj" -c Release -o /app/publish

FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "WebBackEnd.dll"]